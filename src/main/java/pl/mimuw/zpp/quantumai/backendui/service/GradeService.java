package pl.mimuw.zpp.quantumai.backendui.service;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.backendui.model.*;
import pl.mimuw.zpp.quantumai.backendui.mq.GradeRequestProducer;
import pl.mimuw.zpp.quantumai.backendui.repository.GradeRepository;
import pl.mimuw.zpp.quantumai.backendui.storage.Storage;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

import java.io.IOException;
import java.util.Optional;

import static pl.mimuw.zpp.quantumai.backendui.model.Grade.GradeStatus.FAILED;
import static pl.mimuw.zpp.quantumai.backendui.model.Grade.GradeStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final RandomNameGenerator randomNameGenerator;
    private final Storage storage;
    private final GradeRepository gradeRepository;
    private final GradeRequestProducer gradeRequestProducer;
    private final VerificationService verificationService;
    private final EuclideanGraphService euclideanGraphService;

    public String generateGradeRequest(
            String graphId,
            Problem problem,
            MultipartFile solution
    ) throws IOException {
        String gradeId = randomNameGenerator.generateName();
        String filePath = storage.save(solution, gradeId);
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
                            .filePath(filePath)
                            .build()
        );
        return gradeId;
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
