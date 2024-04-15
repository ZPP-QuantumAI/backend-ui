package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.MapGraphCreationRequestDto;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.Graph;
import pl.mimuw.zpp.quantumai.backendui.model.GraphType;
import pl.mimuw.zpp.quantumai.backendui.model.MapGraph;
import pl.mimuw.zpp.quantumai.backendui.repository.GraphRepository;
import pl.mimuw.zpp.quantumai.backendui.repository.MapGraphRepository;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapGraphService {
    private final MapGraphRepository mapGraphRepository;
    private final MapGraphConversionService mapGraphConversionService;
    private final RandomNameGenerator randomNameGenerator;
    private final EuclideanGraphService euclideanGraphService;
    private final GraphRepository graphRepository;

    public String createGraph(MapGraphCreationRequestDto request) {
        String id = randomNameGenerator.generateName();

        MapGraph mapGraph = MapGraph.builder()
                .id(id)
                .name(request.name())
                .nodes(
                        request.nodes().stream()
                                .map(coordinatesInDecimal ->
                                        MapGraph.Coordinates.builder()
                                                .longitudeInDecimal(coordinatesInDecimal.longitudeInDecimal())
                                                .latitudeInDecimal(coordinatesInDecimal.latitudeInDecimal())
                                                .build())
                                .toList()
                )
                .build();

        MapGraph mapGraphWithCenter = mapGraphConversionService.setCenter(mapGraph);

        EuclideanGraph euclideanGraph = mapGraphConversionService.fromMap(mapGraphWithCenter);

        mapGraphRepository.save(mapGraphWithCenter);
        euclideanGraphService.saveGraphWithId(euclideanGraph);
        graphRepository.save(
                Graph.builder()
                        .graphId(id)
                        .graphType(GraphType.MAP)
                        .deleted(false)
                        .build()
        );

        return id;
    }

    public List<MapGraph> getAllGraphs() {
        return mapGraphRepository.findAll();
    }

    public List<MapGraph> getAllGraphs(List<String> graphIds) { return mapGraphRepository.findAllById(graphIds); }

    public Optional<MapGraph> getGraphById(String graphId) {
        return mapGraphRepository.findById(graphId);
    }
}
