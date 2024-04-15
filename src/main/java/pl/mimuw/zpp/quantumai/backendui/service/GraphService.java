package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.GraphDto;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.Graph;
import pl.mimuw.zpp.quantumai.backendui.model.GraphType;
import pl.mimuw.zpp.quantumai.backendui.model.MapGraph;
import pl.mimuw.zpp.quantumai.backendui.repository.GraphRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GraphService {
    private final GraphRepository graphRepository;
    private final MapGraphService mapGraphService;
    private final EuclideanGraphService euclideanGraphService;

    public List<GraphDto> getAllGraphs() {
        List<Graph> graphs = graphRepository.findAll();

        Map<String, Graph> graphMap = graphs.stream()
                .collect(Collectors.toMap(Graph::graphId, Function.identity()));

        List<String> euclideanGraphIds = graphs.stream()
                .filter(graph -> graph.graphType().equals(GraphType.EUCLIDEAN))
                .map(Graph::graphId)
                .toList();

        List<String> mapGraphIds = graphs.stream()
                .filter(graph -> graph.graphType().equals(GraphType.MAP))
                .map(Graph::graphId)
                .toList();

        List<EuclideanGraph> euclideanGraphs = euclideanGraphService.getGraphsByIdIn(euclideanGraphIds);

        List<MapGraph> mapGraphs = mapGraphService.getAllGraphs(mapGraphIds);

        Stream<GraphDto> euclideanGraphDtos = euclideanGraphs.stream()
                .map(euclideanGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.EUCLIDEAN)
                                .graph(euclideanGraph)
                                .deleted(graphMap.get(euclideanGraph.id()).deleted())
                                .build());

        Stream<GraphDto> mapGraphDtos = mapGraphs.stream()
                .map(mapGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.MAP)
                                .graph(mapGraph)
                                .deleted(graphMap.get(mapGraph.id()).deleted())
                                .build());

        return Stream.concat(mapGraphDtos, euclideanGraphDtos).toList();
    }

    public Optional<GraphDto> getGraphById(String graphId) {
        Map<String, Graph> graphMap = graphRepository.findAll().stream()
                .collect(Collectors.toMap(Graph::graphId, Function.identity()));

        Optional<GraphDto> mapGraphDto = mapGraphService.getGraphById(graphId)
                .map(mapGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.MAP)
                                .graph(mapGraph)
                                .deleted(graphMap.get(mapGraph.id()).deleted())
                                .build());

        if (mapGraphDto.isPresent()) {
            return mapGraphDto;
        }

        return euclideanGraphService.getGraphById(graphId)
                .map(euclideanGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.EUCLIDEAN)
                                .graph(euclideanGraph)
                                .deleted(graphMap.get(euclideanGraph.id()).deleted())
                                .build());
    }

    public Map<String, GraphType> getGraphTypes(List<String> graphIds) {
        return graphRepository.findAllById(graphIds).stream()
                .collect(Collectors.toMap(Graph::graphId, Graph::graphType));
    }

    public void deleteGraph(String graphId) {
        graphRepository.findById(graphId).ifPresent(
                graph -> graphRepository.save(graph.withDeleted(true))
        );
    }
}
