package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;

@Builder
public record GradeRequest(
        String gradeId,
        String graphId
) {
}
