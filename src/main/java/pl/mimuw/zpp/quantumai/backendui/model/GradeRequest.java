package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;

import java.util.List;

@Builder
public record GradeRequest(
        List<SingleRequest> requests,
        String solutionId
) {
    @Builder
    public record SingleRequest(
        String gradeId,
        String graphId
    ) {}
}