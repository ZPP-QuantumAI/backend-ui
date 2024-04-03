package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;

@Builder
public record Graph(
        GraphType graphType,
        @Id String graphId
) {
}
