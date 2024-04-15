package pl.mimuw.zpp.quantumai.backendui.controller.dto;

import lombok.Builder;
import pl.mimuw.zpp.quantumai.backendui.model.Solution;

@Builder
public record SolutionDto(
        String solutionId,
        String name
) {
    public static SolutionDto fromSolution(Solution solution) {
        return SolutionDto.builder()
                .solutionId(solution.solutionId())
                .name(solution.name())
                .build();
    }
}
