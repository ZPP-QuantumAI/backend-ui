package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record EuclideanGraph(
        @Id String name,
        List<Node> nodes
) {
    @Builder
    public record Node(
        BigDecimal x,
        BigDecimal y
    ) { }
}
