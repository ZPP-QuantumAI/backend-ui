package pl.mimuw.zpp.quantumai.backendui.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.mimuw.zpp.quantumai.backendui.model.RunResult;
import pl.mimuw.zpp.quantumai.backendui.service.GradeService;

@Component
@RequiredArgsConstructor
@Slf4j
public class RunResultConsumer {
    private final GradeService gradeService;
    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "id", topics = "run-result")
    public void listenToRunResult(String runResultAsJson) {
        try {
            RunResult runResult = objectMapper.readValue(runResultAsJson, RunResult.class);
            log.info("Got RunResult: {}", runResult.toString());
            gradeService.handleRunResult(runResult);
        } catch (JsonProcessingException e) {
            log.error("Error when parsing json", e);
        }
    }
}
