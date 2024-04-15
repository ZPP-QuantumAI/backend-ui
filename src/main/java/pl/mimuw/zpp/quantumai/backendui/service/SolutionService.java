package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.PackageGradeDto;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.SolutionDto;
import pl.mimuw.zpp.quantumai.backendui.error.PackageNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.error.SolutionNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.model.*;
import pl.mimuw.zpp.quantumai.backendui.repository.FileRepository;
import pl.mimuw.zpp.quantumai.backendui.repository.SolutionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final FileRepository fileRepository;
    private final PackageService packageService;
    private final GradeService gradeService;
    private final FinalGradeService finalGradeService;

    public PackageGradeDto getPackageGradeDto(String solutionId) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(() -> new SolutionNotFoundException(solutionId));
        GraphPackage graphPackage = packageService.getGraphPackage(solution.resourceId()).orElseThrow(() -> new PackageNotFoundException(solution.resourceId()));
        List<Grade> grades = gradeService.getGradesFromPackage(solutionId);
        Status status = Status.statusFromMany(
                grades.stream()
                        .map(Grade::status)
                        .toList()
        );
        Long runtimeInMs =
                status.equals(Status.SUCCESS)
                ? grades.stream()
                        .map(Grade::runtimeInMs)
                        .reduce(0L, Long::sum)
                : null;
        PackageGradeDto.FinalGrade finalGrade = PackageGradeDto.FinalGrade.builder()
                .finalGrade(status.equals(Status.SUCCESS) ? finalGradeService.finalGrade(grades) : null)
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

    public List<SolutionDto> getAllByProblem(Problem problem) {
        return solutionRepository.findAllByProblem(problem).stream()
                .map(SolutionDto::fromSolution)
                .toList();
    }

    public void deleteSolution(String solutionId) {
        solutionRepository.deleteById(solutionId);
        fileRepository.deleteById(solutionId);
    }
}
