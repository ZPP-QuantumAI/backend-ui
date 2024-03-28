package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.GraphDto;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.GraphType;
import pl.mimuw.zpp.quantumai.backendui.model.MapGraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GraphService {
    private final MapGraphService mapGraphService;
    private final EuclideanGraphService euclideanGraphService;
    public List<GraphDto> getAllGraphs() {
        List<MapGraph> mapGraphs = mapGraphService.getAllGraphs();

        List<String> mapGraphIds = mapGraphs.stream()
                .map(MapGraph::id)
                .toList();

        List<EuclideanGraph> euclideanGraphs = euclideanGraphService.getAllGraphs();

        List<EuclideanGraph> euclideanGraphsThatAreNotMaps = euclideanGraphs.stream()
                .filter(euclideanGraph -> !mapGraphIds.contains(euclideanGraph.id()))
                .toList();

        Stream<GraphDto> mapGraphDtos = mapGraphs.stream()
                .map(mapGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.MAP)
                                .graph(mapGraph)
                                .build());

        Stream<GraphDto> euclideanGraphDtos = euclideanGraphsThatAreNotMaps.stream()
                .map(euclideanGraph ->
                        GraphDto.builder()
                                .graphType(GraphType.EUCLIDEAN)
                                .graph(euclideanGraph)
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
}
