package pl.mimuw.zpp.quantumai.backendui.model;

public record RunResult(
        String gradeId,
        String output,
        Long runtimeInMs
) {
}
