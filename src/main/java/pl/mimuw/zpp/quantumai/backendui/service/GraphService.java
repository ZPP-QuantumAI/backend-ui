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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GraphService {
    private final GraphRepository graphRepository;
    private final MapGraphService mapGraphService;
    private final EuclideanGraphService euclideanGraphService;

    public void createGraph(String graphId, GraphType graphType) {
        graphRepository.save(
                Graph.builder()
                        .graphId(graphId)
                        .graphType(graphType)
                        .build()
        );
    }
    public List<GraphDto> getAllGraphs() {
        List<Graph> graphs = graphRepository.findAll();

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
                                .build());

        Stream<GraphDto> mapGraphDtos = mapGraphs.stream()
                .map(mapGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.MAP)
                                .graph(mapGraph)
                                .build());

        return Stream.concat(mapGraphDtos, euclideanGraphDtos).toList();
    }

    public Optional<GraphDto> getGraphById(String graphId) {
        Optional<GraphDto> mapGraphDto = mapGraphService.getGraphById(graphId)
                .map(mapGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.MAP)
                                .graph(mapGraph)
                                .build());

        if (mapGraphDto.isPresent()) {
            return mapGraphDto;
        }

        return euclideanGraphService.getGraphById(graphId)
                .map(euclideanGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.EUCLIDEAN)
                                .graph(euclideanGraph)
                                .build());
    }

    public Map<String, GraphType> getGraphTypes(List<String> graphIds) {
        return graphRepository.findAllById(graphIds).stream()
                .collect(Collectors.toMap(Graph::graphId, Graph::graphType));
    }
}
