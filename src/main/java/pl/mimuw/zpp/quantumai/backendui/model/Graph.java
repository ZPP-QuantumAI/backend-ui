package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;

@Builder
@With
public record Graph(
        GraphType graphType,
        Boolean deleted,
        @Id String graphId
) {
}
