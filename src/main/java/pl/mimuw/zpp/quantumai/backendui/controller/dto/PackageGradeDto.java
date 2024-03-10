package pl.mimuw.zpp.quantumai.backendui.controller.dto;

import lombok.Builder;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.GraphPackage;
import pl.mimuw.zpp.quantumai.backendui.model.Status;

import java.util.List;

@Builder
public record PackageGradeDto(
        String solutionId,
        GraphPackage graphPackage,
        Status status,
        Long runtimeInMs,
        String algorithmName,
        FinalGrade finalGrade

) {
    @Builder
    public record FinalGrade(
        Object finalGrade,
        List<Grade> grades
    ) {}
}
