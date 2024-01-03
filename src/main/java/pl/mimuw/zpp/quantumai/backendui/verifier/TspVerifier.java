package pl.mimuw.zpp.quantumai.backendui.verifier;

import io.vavr.control.Either;
import org.springframework.stereotype.Component;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;
import pl.mimuw.zpp.quantumai.backendui.verifier.result.TspResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static pl.mimuw.zpp.quantumai.backendui.model.Problem.TSP;

@Component
public class TspVerifier implements Verifier {
    private static final int STARTING_VERTEX_NUMBER = 0;

    @Override
    public Problem problem() {
        return TSP;
    }

    @Override
    public Either<String, Object> verify(EuclideanGraph graph, String programOutput) {
        List<Integer> permutation = getPermutationFromOutput(programOutput);

        if (!hasMatchingSize(graph, permutation))
            return Either.left("output's length should be equal to number of vertices plus one");

        if (!permutation.get(0).equals(STARTING_VERTEX_NUMBER))
            return Either.left("the first vertex should be starting vertex");

        if (!permutation.get(permutation.size() - 1).equals(STARTING_VERTEX_NUMBER))
            return Either.left("the last vertex should be starting vertex");

        if (hasInvalidVertexNumbers(graph, permutation))
            return Either.left("the permutation should have vertex numbers in range [0, max vertex number)");

        if (hasDuplicates(permutation))
            return Either.left("the permutation contains duplicates");

        BigDecimal weightsSum = weightsSum(graph, permutation);

        return Either.right(
                TspResult.builder()
                         .sumOfWeights(weightsSum)
                         .permutation(permutation)
                         .build()
        );
    }

    private List<Integer> getPermutationFromOutput(String programOutput) {
        Scanner scanner = new Scanner(programOutput);
        List<Integer> result = new ArrayList<>();
        while (scanner.hasNextInt()) {
            result.add(scanner.nextInt());
        }
        return result;
    }

    private boolean hasMatchingSize(EuclideanGraph graph, List<Integer> permutation) {
        return graph.nodes().size() + 1 == permutation.size();
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

    private BigDecimal getWeight(EuclideanGraph graph, int from, int to) {
        EuclideanGraph.Node nodeFrom = graph.nodes().get(from),
                nodeTo = graph.nodes().get(to);
        BigDecimal dx = nodeFrom.x().subtract(nodeTo.x()),
                dy = nodeFrom.y().subtract(nodeTo.y());
        return dx.pow(2).add(dy.pow(2)).sqrt(MathContext.DECIMAL64);
    }

    private BigDecimal weightsSum(EuclideanGraph graph, List<Integer> permutation) {
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i < permutation.size() - 1; i++) {
            result = result.add(
                    getWeight(graph, permutation.get(i), permutation.get(i + 1))
            );
        }
        return result;
    }
}
