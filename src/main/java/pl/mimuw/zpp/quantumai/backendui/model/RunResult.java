package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import lombok.ToString;
import lombok.With;

@Builder
@With
public record RunResult(
        String gradeId,
        boolean success,
        String output,
        Long runtimeInMs
) {
}
