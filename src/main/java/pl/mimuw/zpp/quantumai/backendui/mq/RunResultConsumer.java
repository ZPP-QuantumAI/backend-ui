package pl.mimuw.zpp.quantumai.backendui.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.mimuw.zpp.quantumai.backendui.model.RunResult;
import pl.mimuw.zpp.quantumai.backendui.service.GradeService;

@Component
@RequiredArgsConstructor
public class RunResultConsumer {
    private final GradeService gradeService;

    public void onMessage(RunResult runResult) {
        // TODO
        gradeService.handleRunResult(runResult);
    }
}
