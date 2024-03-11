package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.PackageGradeDto;
import pl.mimuw.zpp.quantumai.backendui.error.InvalidSolutionTypeException;
import pl.mimuw.zpp.quantumai.backendui.error.PackageNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.error.SolutionNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.GraphPackage;
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
    private final FinalGradeService finalGradeService;

    public PackageGradeDto getPackageGradeDto(String solutionId) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(() -> new SolutionNotFoundException(solutionId));
        if (!solution.solutionType().equals(Solution.SolutionType.PACKAGE)) {
            throw new InvalidSolutionTypeException(Solution.SolutionType.PACKAGE, solution.solutionType());
        }
        GraphPackage graphPackage = packageService.getGraphPackage(solution.resourceId()).orElseThrow(() -> new PackageNotFoundException(solution.resourceId()));
        List<Grade> grades = gradeService.getGradesFromPackage(solutionId);
        Status status = Status.statusFromMany(
                grades.stream()
                        .map(Grade::status)
                        .toList()
        );
        Long runtimeInMs = grades.stream()
                .map(Grade::runtimeInMs)
                .reduce(0L, Long::sum);
        PackageGradeDto.FinalGrade finalGrade = PackageGradeDto.FinalGrade.builder()
                .finalGrade(finalGradeService.finalGrade(grades))
                .grades(grades)
                .build();
        return PackageGradeDto.builder()
                .solutionId(solutionId)
                .graphPackage(graphPackage)
                .status(status)
                .runtimeInMs(runtimeInMs)
                .finalGrade(finalGrade)
                .algorithmName(solution.name())
                .build();
    }
}
