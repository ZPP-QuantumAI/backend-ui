package pl.mimuw.zpp.quantumai.backendui.controller.dto;

import lombok.Builder;
import pl.mimuw.zpp.quantumai.backendui.model.GraphType;

@Builder
public record GraphDto(
        GraphType graphType,
        Boolean deleted,
        Object graph
) {
}
