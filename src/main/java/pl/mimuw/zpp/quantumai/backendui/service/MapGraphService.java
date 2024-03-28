package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.MapGraphCreationRequestDto;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.MapGraph;
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

    public String createGraph(MapGraphCreationRequestDto request) {
        String id = randomNameGenerator.generateName();

        List<MapGraph.Coordinates> coordinates = request.coordinatesInDecimal().stream()
                .map(coordsInDecimal -> mapGraphConversionService.fromDecimal(coordsInDecimal.longitudeInDecimal(), coordsInDecimal.latitudeInDecimal()))
                .toList();

        MapGraph mapGraph = MapGraph.builder()
                .id(id)
                .name(request.name())
                .coordinates(coordinates)
                .build();

        MapGraph mapGraphWithCenterAndScale = mapGraphConversionService.setCenterAndScale(mapGraph);

        EuclideanGraph euclideanGraph = mapGraphConversionService.fromMap(mapGraphWithCenterAndScale);

        mapGraphRepository.save(mapGraphWithCenterAndScale);
        euclideanGraphService.saveGraphWithId(euclideanGraph);

        return id;
    }

    public List<MapGraph> getAllGraphs() {
        return mapGraphRepository.findAll();
    }

    public Optional<MapGraph> getGraphById(String graphId) {
        return mapGraphRepository.findById(graphId);
    }
}
