package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Builder
@With
public record EuclideanGraph(
        @Id String id,
        String name,
        List<Node> nodes
) {
    @Builder
    public record Node(
        BigDecimal x,
        BigDecimal y
    ) { }
}