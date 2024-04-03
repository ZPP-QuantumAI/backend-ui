package pl.mimuw.zpp.quantumai.backendui.service;

import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.MapGraph;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Objects;

@Service
public class MapGraphConversionService {
    /*
        All the formulas come from here: https://en.wikipedia.org/wiki/Equirectangular_projection#Forward
     */
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
    private static final BigDecimal RADIUS_OF_GLOBE_IN_KM = BigDecimal.valueOf(6371.008);
    private static final BigDecimal ONE_DEGREE_IN_RADIANS = BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(180), MATH_CONTEXT);

    public MapGraph setCenter(MapGraph mapGraph) {
        BigDecimal numberOfCoords = BigDecimal.valueOf(mapGraph.nodes().size());

        BigDecimal averageLongitudeInDecimal = mapGraph.nodes().stream()
                .map(MapGraph.Coordinates::longitudeInDecimal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(numberOfCoords, MATH_CONTEXT);

        BigDecimal averageLatitudeInDecimal = mapGraph.nodes().stream()
                .map(MapGraph.Coordinates::latitudeInDecimal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(numberOfCoords, MATH_CONTEXT);

        return mapGraph
                .withCenterOfMap(
                        MapGraph.Coordinates.builder()
                                .longitudeInDecimal(averageLongitudeInDecimal)
                                .latitudeInDecimal(averageLatitudeInDecimal)
                                .build()
                );
    }

    public EuclideanGraph fromMap(MapGraph mapGraph) {
        if (Objects.isNull(mapGraph.centerOfMap())) {
            throw new RuntimeException("centerOfMap of map graph is required");
        }

        BigDecimal averageLatitudeInDecimal = mapGraph.centerOfMap().latitudeInDecimal();

        BigDecimal averageLatitudeInRadians = averageLatitudeInDecimal.multiply(ONE_DEGREE_IN_RADIANS);

        BigDecimal scale = BigDecimal.valueOf(Math.cos(averageLatitudeInRadians.doubleValue()));

        List<EuclideanGraph.Node> nodes = mapGraph.nodes().stream()
                .map(coords -> fromCoordinates(coords, mapGraph.centerOfMap(), scale))
                .toList();

        return EuclideanGraph.builder()
                .id(mapGraph.id())
                .name(mapGraph.name())
                .nodes(nodes)
                .build();
    }

    private EuclideanGraph.Node fromCoordinates(
            MapGraph.Coordinates coordinates,
            MapGraph.Coordinates centerOfMap,
            BigDecimal scale
    ) {
        BigDecimal longitudeInRadians = coordinates.longitudeInDecimal().multiply(ONE_DEGREE_IN_RADIANS);
        BigDecimal centerLongitudeInRadians = centerOfMap.longitudeInDecimal().multiply(ONE_DEGREE_IN_RADIANS);
        BigDecimal deltaLongitude = longitudeInRadians.subtract(centerLongitudeInRadians);
        BigDecimal x = RADIUS_OF_GLOBE_IN_KM.add(deltaLongitude).multiply(scale);

        BigDecimal latitudeInRadians = coordinates.latitudeInDecimal().multiply(ONE_DEGREE_IN_RADIANS);
        BigDecimal centerLatitudeInRadians = centerOfMap.latitudeInDecimal().multiply(ONE_DEGREE_IN_RADIANS);
        BigDecimal deltaLatitude = latitudeInRadians.subtract(centerLatitudeInRadians);
        BigDecimal y = RADIUS_OF_GLOBE_IN_KM.multiply(deltaLatitude);

        return EuclideanGraph.Node.builder()
                .x(x)
                .y(y)
                .build();
    }
}
