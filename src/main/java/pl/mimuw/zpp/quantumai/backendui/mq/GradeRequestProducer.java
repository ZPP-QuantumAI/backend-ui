package pl.mimuw.zpp.quantumai.backendui.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.mimuw.zpp.quantumai.backendui.model.GradeRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class GradeRequestProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public void generateGradeRequest(GradeRequest request) {
        try {
            String requestAsJson = objectMapper.writeValueAsString(request);
            var sendResult = kafkaTemplate.send("grade-request", requestAsJson);
            sendResult.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Sent message=[" + requestAsJson + "]");
                } else {
                    log.error("Unable to send message=[" + requestAsJson + "]", ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Unable to send message, because of Json exception", e);
        }
    }
}
