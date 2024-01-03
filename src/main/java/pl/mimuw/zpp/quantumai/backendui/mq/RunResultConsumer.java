package pl.mimuw.zpp.quantumai.backendui.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.mimuw.zpp.quantumai.backendui.model.RunResult;
import pl.mimuw.zpp.quantumai.backendui.service.GradeService;

import java.util.Optional;
import java.util.Queue;

@Component
@RequiredArgsConstructor
@Slf4j
public class RunResultConsumer {
    private final GradeService gradeService;
    private final Queue<RunResult> runResultQueue;

    @Scheduled(fixedDelay = 10000L)
    public void lookForNewMessage() {
        Optional.ofNullable(runResultQueue.poll())
                .ifPresent(this::handleRunResult);
    }

    private void handleRunResult(RunResult result) {
        log.info("Got RunResult: {}", result.toString());
        gradeService.handleRunResult(result);
    }
}
