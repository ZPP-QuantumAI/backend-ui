package pl.mimuw.zpp.quantumai.backendui.controller.dto;

import lombok.Builder;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.Status;

import java.util.List;

@Builder
public record PackageGradeDto(
        String solutionId,
        GraphPackageDto graphPackage,
        Status status,
        List<Grade> grades

) {
}
