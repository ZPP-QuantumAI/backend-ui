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

    public MapGraph.Coordinates fromDecimal(BigDecimal longitudeInDecimal, BigDecimal latitudeInDecimal) {
        return MapGraph.Coordinates.builder()
                .longitudeInRadians(longitudeInDecimal.multiply(ONE_DEGREE_IN_RADIANS))
                .latitudeInRadians(latitudeInDecimal.multiply(ONE_DEGREE_IN_RADIANS))
                .build();
    }

    public MapGraph setCenterAndScale(MapGraph mapGraph) {
        BigDecimal numberOfCoords = BigDecimal.valueOf(mapGraph.coordinates().size());

        BigDecimal averageLongitude = mapGraph.coordinates().stream()
                .map(MapGraph.Coordinates::longitudeInRadians)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(numberOfCoords, MATH_CONTEXT);

        BigDecimal averageLatitude = mapGraph.coordinates().stream()
                .map(MapGraph.Coordinates::latitudeInRadians)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(numberOfCoords, MATH_CONTEXT);

        BigDecimal scale = BigDecimal.valueOf(Math.cos(averageLatitude.doubleValue()));

        return mapGraph
                .withCenterOfMap(
                        MapGraph.Coordinates.builder()
                                .longitudeInRadians(averageLongitude)
                                .latitudeInRadians(averageLatitude)
                                .build()
                )
                .withScale(scale);
    }

    public EuclideanGraph fromMap(MapGraph mapGraph) {
        if (Objects.isNull(mapGraph.centerOfMap())) {
            throw new RuntimeException("centerOfMap of map graph is required");
        }

        if (Objects.isNull(mapGraph.scale())) {
            throw new RuntimeException("scale of map is required");
        }

        List<EuclideanGraph.Node> nodes = mapGraph.coordinates().stream()
                .map(coords -> fromCoordinates(coords, mapGraph.centerOfMap(), mapGraph.scale()))
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
        BigDecimal deltaLongitude = coordinates.longitudeInRadians().subtract(centerOfMap.longitudeInRadians());
        BigDecimal x = RADIUS_OF_GLOBE_IN_KM.add(deltaLongitude).multiply(scale);

        BigDecimal deltaLatitude = coordinates.latitudeInRadians().subtract(centerOfMap.latitudeInRadians());
        BigDecimal y = RADIUS_OF_GLOBE_IN_KM.multiply(deltaLatitude);

        return EuclideanGraph.Node.builder()
                .x(x)
                .y(y)
                .build();
    }
}
