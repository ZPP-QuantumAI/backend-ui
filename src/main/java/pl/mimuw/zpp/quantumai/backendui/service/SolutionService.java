package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.GraphPackageDto;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.PackageGradeDto;
import pl.mimuw.zpp.quantumai.backendui.error.InvalidSolutionTypeException;
import pl.mimuw.zpp.quantumai.backendui.error.PackageNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.error.SolutionNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.Solution;
import pl.mimuw.zpp.quantumai.backendui.model.Status;
import pl.mimuw.zpp.quantumai.backendui.repository.SolutionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final PackageService packageService;
    private final GradeService gradeService;

    public PackageGradeDto getPackageGradeDto(String solutionId) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(() -> new SolutionNotFoundException(solutionId));
        if (!solution.solutionType().equals(Solution.SolutionType.PACKAGE)) {
            throw new InvalidSolutionTypeException(Solution.SolutionType.PACKAGE, solution.solutionType());
        }
        GraphPackageDto graphPackageDto = packageService.getGraphPackageDto(solution.resourceId()).orElseThrow(() -> new PackageNotFoundException(solution.resourceId()));
        List<Grade> grades = gradeService.getGradesFromPackage(solutionId);
        Status status = Status.statusFromMany(
                grades.stream()
                        .map(Grade::status)
                        .toList()
        );
        return PackageGradeDto.builder()
                .solutionId(solutionId)
                .graphPackage(graphPackageDto)
                .status(status)
                .grades(grades)
                .build();
    }
}
