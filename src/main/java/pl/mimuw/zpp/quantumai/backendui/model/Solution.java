package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;

@Builder
public record Solution(
        @Id String solutionId,
        SolutionType solutionType,
        String resourceId
) {
    public enum SolutionType {
        GRAPH,
        PACKAGE
    }
}
