package pl.mimuw.zpp.quantumai.backendui.service;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.backendui.model.*;
import pl.mimuw.zpp.quantumai.backendui.mq.GradeRequestProducer;
import pl.mimuw.zpp.quantumai.backendui.repository.GradeRepository;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

import java.util.Optional;

import static pl.mimuw.zpp.quantumai.backendui.model.Grade.GradeStatus.*;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final RandomNameGenerator randomNameGenerator;
    private final StorageService storageService;
    private final GradeRepository gradeRepository;
    private final GradeRequestProducer gradeRequestProducer;
    private final VerificationService verificationService;
    private final EuclideanGraphService euclideanGraphService;

    public String generateGradeRequest(
            String graphId,
            Problem problem,
            MultipartFile solution
    ) {
        String gradeId = randomNameGenerator.generateName();
        storageService.save(solution, gradeId);
        gradeRepository.save(
                Grade.builder()
                            .gradeId(gradeId)
                            .graphId(graphId)
                            .problem(problem)
                            .status(Grade.GradeStatus.WAITING)
                            .build()
        );
        gradeRequestProducer.generateGradeRequest(
                GradeRequest.builder()
                            .gradeId(gradeId)
                            .graphId(graphId)
                            .build()
        );
        return gradeId;
    }

    public void handleRunResult(RunResult runResult) {
        Grade grade = gradeRepository.findById(runResult.gradeId()).orElseThrow(RuntimeException::new);

        Either<String, Object> verificationResult =
                euclideanGraphService.getGraph(grade.graphId())
                        .<Either<String, EuclideanGraph>>map(Either::right)
                        .orElse(Either.left("no such graph in the db"))
                        .flatMap(graph -> verificationService.verify(graph, grade.problem(), runResult.output()));

        gradeRepository.save(
                verificationResult.isRight()
                    ? grade.withStatus(SUCCESS)
                        .withRuntimeInMs(grade.runtimeInMs())
                        .withResult(verificationResult.get())
                    : grade.withStatus(FAILED)
                        .withResult(verificationResult.getLeft())
        );
    }

    public Optional<Grade> getGrade(
            String gradeId
    ) {
        return gradeRepository.findById(gradeId);
    }
}
