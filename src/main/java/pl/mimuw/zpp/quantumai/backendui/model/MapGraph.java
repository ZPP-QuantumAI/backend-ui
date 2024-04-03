package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Builder
@With
public record MapGraph(
        @Id String id,
        String name,
        List<Coordinates> nodes,
        Coordinates centerOfMap
) {
    @Builder
    public record Coordinates(
        BigDecimal longitudeInDecimal,
        BigDecimal latitudeInDecimal
    ) {}
}
