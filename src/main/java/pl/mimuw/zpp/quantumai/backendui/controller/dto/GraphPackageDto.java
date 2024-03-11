package pl.mimuw.zpp.quantumai.backendui.controller.dto;

import lombok.Builder;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;

import java.util.List;

@Builder
public record GraphPackageDto(
        String packageId,
        String name,
        List<EuclideanGraph> graphs
) {
}
