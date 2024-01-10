package pl.mimuw.zpp.quantumai.backendui.solver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.GradeRequest;
import pl.mimuw.zpp.quantumai.backendui.model.RunResult;
import pl.mimuw.zpp.quantumai.backendui.service.EuclideanGraphService;
import pl.mimuw.zpp.quantumai.backendui.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Queue;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolveService {
    private final EuclideanGraphService euclideanGraphService;
    private final Storage storage;
    private final Queue<GradeRequest> gradeRequestQueue;
    private final Queue<RunResult> runResultQueue;

    @Scheduled(fixedDelay = 10000L)
    public void lookForNewGradeRequest() {
        Optional.ofNullable(gradeRequestQueue.poll())
                .ifPresent(this::handleGradeRequest);
    }

    private void handleGradeRequest(GradeRequest request) {
        request.requests().forEach(
                singleRequest -> handleSingleRequest(singleRequest, request.solutionId())
        );
    }

    private void handleSingleRequest(GradeRequest.SingleRequest request, String filePath) {
        try {
            EuclideanGraph graph = getGraph(request.graphId());
            String inputAsString = graphToInput(graph);
            File input = inputFromString(inputAsString);
            File zippedProgram = storage.get(filePath);
            Path unzippedProgramPath = unzipProgram(zippedProgram);
            RunResult result = run(unzippedProgramPath, input);
            runResultQueue.offer(result.withGradeId(request.gradeId()));
        } catch (Exception e) {
            runResultQueue.offer(
                    RunResult.builder()
                            .gradeId(request.gradeId())
                            .success(false)
                            .build()
            );
        }
    }

    private EuclideanGraph getGraph(String graphId) {
        return euclideanGraphService.getGraph(graphId).orElseThrow(RuntimeException::new);
    }

    private String graphToInput(EuclideanGraph graph) {
        StringBuilder sb = new StringBuilder();
        sb.append(graph.nodes().size());
        sb.append('\n');
        for (var node : graph.nodes()) {
            sb.append(node.x());
            sb.append(' ');
            sb.append(node.y());
            sb.append('\n');
        }
        return sb.toString();
    }

    private File inputFromString(String input) throws IOException {
        Path filePath = Files.createTempFile("graph", null);
        Files.write(filePath, input.getBytes(), StandardOpenOption.WRITE);
        return filePath.toFile();
    }

    private Path unzipProgram(File zippedFile) throws IOException, InterruptedException {
        String zippedFilePath = zippedFile.getAbsolutePath();
        Path outputDirectory = Files.createTempDirectory("solution");
        log.info("zippedFilePath: {}, outputDirectoryPath: {}", zippedFile, outputDirectory.toString());
        new ProcessBuilder("unzip", zippedFilePath, "-d", outputDirectory.toString())
                .start()
                .waitFor();
        return outputDirectory;
    }

    private RunResult run(Path unzippedProgram, File inputFile) throws IOException, InterruptedException {
        File outputFile = File.createTempFile("output", null);
        Instant start = Instant.now();
        String programPath = unzippedProgram.toString() + "/run.py";
        log.info("Program path: {}", programPath);
        new ProcessBuilder("python3", programPath)
                .redirectInput(inputFile)
                .redirectOutput(outputFile)
                .start()
                .waitFor();
        Instant end = Instant.now();
        String result = Files.readString(Path.of(outputFile.getAbsolutePath()));
        outputFile.delete();
        return RunResult.builder()
                .success(true)
                .output(result)
                .runtimeInMs(Duration.between(start, end).toMillis())
                .build();
    }
}
