package pl.mimuw.zpp.quantumai.backendui.service;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.feign.UtmPythonFeignClient;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.TspResult;
import pl.mimuw.zpp.quantumai.backendui.repository.EuclideanGraphRepository;
import pl.mimuw.zpp.quantumai.backendui.utils.Utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SolveService {
    private static final String NEWLINE = "\n";
    private static final String SPACE = " ";
    private static final int STARTING_VERTEX_NUMBER = 0;
    private final EuclideanGraphRepository euclideanGraphRepository;
    private final UtmPythonFeignClient utmPythonFeignClient;

    public Either<String, TspResult> solveTsp(String pythonCode, String graphName) throws Exception {
        Either<String, EuclideanGraph> graph = getGraph(graphName);
        return Utils.map2(
                () -> graph,
                () -> getOutputOfExecutedCode(pythonCode, graph),
                this::verifyOutput
        ).flatMap(Function.identity());
    }

    private Either<String, String> getOutputOfExecutedCode(String pythonCode, Either<String, EuclideanGraph> graph) throws Exception {
        return Utils.map2(
                () -> Either.right(pythonCode),
                () -> graph.map(this::graphToString),
                this::executeCodeOnInput
        ).flatMap(Function.identity());
    }

    private Either<String, EuclideanGraph> getGraph(String graphName) {
        return euclideanGraphRepository.findById(graphName)
                .<Either<String, EuclideanGraph>>map(Either::right)
                .orElse(Either.left("graph with the given name does not exist"));
    }

    private String graphToString(EuclideanGraph graph) {
        return graph.nodes().size() + NEWLINE +
                String.join(
                    NEWLINE,
                    graph.nodes().stream()
                            .map(node -> node.x().toString() + SPACE + node.y().toString())
                            .toList()
                );
    }

    private Either<String, String> executeCodeOnInput(String code, String input) {
        ResponseEntity<String> response = utmPythonFeignClient.solve(code, input);
        return response.getStatusCode().is2xxSuccessful()
                ? Either.right(response.getBody())
                : Either.left("something went wrong while executing code");
    }

    private Either<String, TspResult> verifyOutput(EuclideanGraph graph, String output) {
        List<Integer> permutation = permutationFromString(output);
        if (!hasMatchingSize(graph, permutation)) {
            return Either.left("output's length should be equal to number of vertices plus one");
        }
        if (!permutation.get(0).equals(STARTING_VERTEX_NUMBER)) {
            return Either.left("the first vertex should be starting vertex");
        }
        if (!permutation.get(permutation.size() - 1).equals(STARTING_VERTEX_NUMBER)) {
            return Either.left("the last vertex should be starting vertex");
        }
        if (hasInvalidVertexNumbers(graph, permutation)) {
            return Either.left("the permutation should have vertex numbers in range [0, max vertex number)");
        }
        if (hasDuplicates(permutation)) {
            return Either.left("the permutation contains duplicates");
        }

        BigDecimal sumOfWeights = Stream.iterate(0, i -> i + 1)
                .limit(permutation.size() - 1)
                .map(i -> getWeight(graph, permutation.get(i), permutation.get(i + 1)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Either.right(
                TspResult.builder()
                        .sumOfWeights(sumOfWeights)
                        .permutation(permutation)
                        .build()
        );
    }

    private List<Integer> permutationFromString(String string) {
        List<Integer> result = new ArrayList<>();
        Scanner scanner = new Scanner(string);
        while (scanner.hasNextInt()) {
            result.add(scanner.nextInt());
        }
        return result;
    }

    private boolean hasMatchingSize(EuclideanGraph graph, List<Integer> output) {
        return graph.nodes().size() + 1 == output.size();
    }

    private boolean hasInvalidVertexNumbers(EuclideanGraph graph, List<Integer> permutation) {
        return permutation.stream()
                .anyMatch(v -> v < 0 || v >= graph.nodes().size());
    }

    private boolean hasDuplicates(List<Integer> permutation) {
        boolean[] used = new boolean[permutation.size() - 1];
        for (int i = 0; i < permutation.size() - 1; i++) {
            Integer el = permutation.get(i);
            if (used[el]) {
                return true;
            } else {
                used[el] = true;
            }
        }
        return false;
    }

    private BigDecimal getWeight(EuclideanGraph graph, int a, int b) {
        EuclideanGraph.Node nodeA = graph.nodes().get(a),
                nodeB = graph.nodes().get(b);
        BigDecimal dx = nodeA.x().subtract(nodeB.x()),
                dy = nodeA.y().subtract(nodeB.y());
        return dx.pow(2).add(dy.pow(2)).sqrt(MathContext.DECIMAL64);
    }
}
