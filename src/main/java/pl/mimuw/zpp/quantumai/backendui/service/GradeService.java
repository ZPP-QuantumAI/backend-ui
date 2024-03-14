package pl.mimuw.zpp.quantumai.backendui.service;

import io.vavr.control.Either;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.PackageGradeRequestDto;
import pl.mimuw.zpp.quantumai.backendui.model.*;
import pl.mimuw.zpp.quantumai.backendui.mq.GradeRequestProducer;
import pl.mimuw.zpp.quantumai.backendui.repository.GradeRepository;
import pl.mimuw.zpp.quantumai.backendui.repository.GraphPackageRepository;
import pl.mimuw.zpp.quantumai.backendui.repository.SolutionRepository;
import pl.mimuw.zpp.quantumai.backendui.storage.Storage;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

import java.io.IOException;
import java.time.Clock;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static pl.mimuw.zpp.quantumai.backendui.model.Status.FAILED;
import static pl.mimuw.zpp.quantumai.backendui.model.Status.SUCCESS;


@Service
@RequiredArgsConstructor
public class GradeService {
    private final RandomNameGenerator randomNameGenerator;
    private final Storage storage;
    private final GradeRepository gradeRepository;
    private final GradeRequestProducer gradeRequestProducer;
    private final VerificationService verificationService;
    private final EuclideanGraphService euclideanGraphService;
    private final GraphPackageRepository graphPackageRepository;
    private final SolutionRepository solutionRepository;
    private final Clock clock;

    public String generateGradeRequestForPackage(
            PackageGradeRequestDto packageGradeRequestDto
    ) throws IOException {
        GraphPackage graphPackage = graphPackageRepository.findById(packageGradeRequestDto.packageId()).orElseThrow(RuntimeException::new);
        String filePath = storage.save(packageGradeRequestDto.solution());
        String solutionId = randomNameGenerator.generateName();
        solutionRepository.save(
                Solution.builder()
                        .solutionId(solutionId)
                        .solutionType(Solution.SolutionType.PACKAGE)
                        .resourceId(packageGradeRequestDto.packageId())
                        .name(packageGradeRequestDto.name())
                        .build()
        );
        List<GraphGrade> graphGrades = graphPackage.graphIds().stream()
                .map(graphId -> GraphGrade.builder()
                        .graphId(graphId)
                        .gradeId(randomNameGenerator.generateName())
                        .build())
                .toList();
        Map<String, EuclideanGraph> graphMap = getGraphIdToGraphMap(graphPackage.graphIds());
        graphGrades.forEach(graphGrade -> saveWaitingGradeInDb(graphGrade.gradeId, graphGrade.graphId, solutionId, packageGradeRequestDto.problem(), graphMap));
        gradeRequestProducer.generateGradeRequest(
                GradeRequest.builder()
                        .requests(
                                graphGrades.stream()
                                        .map(graphGrade ->
                                                GradeRequest.SingleRequest.builder()
                                                        .gradeId(graphGrade.gradeId)
                                                        .graphId(graphGrade.graphId)
                                                        .build())
                                        .toList()
                        )
                        .solutionId(filePath)
                        .build()
        );
        return solutionId;
    }

    public void handleRunResult(RunResult runResult) {
        Grade grade = gradeRepository.findById(runResult.gradeId()).orElseThrow(RuntimeException::new);

        if (!runResult.success()) {
            gradeRepository.save(grade.withStatus(FAILED));
            return;
        }

        Either<String, Object> verificationResult =
                euclideanGraphService.getGraph(grade.graphId())
                        .<Either<String, EuclideanGraph>>map(Either::right)
                        .orElse(Either.left("no such graph in the db"))
                        .flatMap(graph -> verificationService.verify(graph, grade.problem(), runResult.output()));

        gradeRepository.save(
                verificationResult.isRight()
                    ? grade.withStatus(SUCCESS)
                        .withRuntimeInMs(runResult.runtimeInMs())
                        .withResult(verificationResult.get())
                        .withTimestamp(clock.instant())
                    : grade.withStatus(FAILED)
                        .withResult(verificationResult.getLeft())
                        .withTimestamp(clock.instant())
        );
    }

    public Optional<Grade> getGrade(
            String gradeId
    ) {
        return gradeRepository.findById(gradeId);
    }

    public List<Grade> getGrades() {
        return gradeRepository.findAll();
    }

    public List<Grade> getGradesFromPackage(String solutionId) {
        List<Grade> grades = gradeRepository.findBySolutionId(solutionId);
        return getLatestForAllGraphs(grades);
    }

    private List<Grade> getLatestForAllGraphs(List<Grade> grades) {
        Map<String, List<Grade>> gradesGroupedByGraphs = grades.stream().collect(Collectors.groupingBy(Grade::graphId));
        return gradesGroupedByGraphs.values().stream()
                .map(this::getLatest)
                .toList();
    }

    private Grade getLatest(List<Grade> grades) {
        return grades.stream()
                .max(Comparator.comparing(Grade::timestamp))
                .orElseThrow(RuntimeException::new);
    }

    private void saveWaitingGradeInDb(String gradeId, String graphId, String solutionId, Problem problem, Map<String, EuclideanGraph> graphMap) {
        gradeRepository.save(
                Grade.builder()
                        .gradeId(gradeId)
                        .graphId(graphId)
                        .graphName(getGraphNameOrNull(graphMap, graphId))
                        .solutionId(solutionId)
                        .problem(problem)
                        .status(Status.WAITING)
                        .build()
        );
    }

    private Map<String, EuclideanGraph> getGraphIdToGraphMap(List<String> graphIds) {
        return euclideanGraphService.getGraphs(graphIds)
                .stream()
                .collect(Collectors.toMap(EuclideanGraph::id, Function.identity()));
    }

    private String getGraphNameOrNull(Map<String, EuclideanGraph> graphMap, String graphId) {
        return Optional.ofNullable(graphMap.get(graphId))
                .map(EuclideanGraph::name)
                .orElse(null);
    }

    @Builder
    private record GraphGrade(
            String graphId,
            String gradeId
    ) {}
}
