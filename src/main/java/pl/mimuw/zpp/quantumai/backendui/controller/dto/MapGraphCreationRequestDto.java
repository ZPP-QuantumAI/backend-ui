package pl.mimuw.zpp.quantumai.backendui.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record MapGraphCreationRequestDto(
        String name,
        List<CoordinatesInDecimal> nodes
) {
    public record CoordinatesInDecimal(
        BigDecimal longitudeInDecimal,
        BigDecimal latitudeInDecimal
    ) {}
}
