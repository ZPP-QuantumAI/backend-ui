package pl.mimuw.zpp.quantumai.backendui.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mimuw.zpp.quantumai.backendui.model.GradeRequest;
import pl.mimuw.zpp.quantumai.backendui.model.RunResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
public class MQConfig {
    @Bean
    public Queue<GradeRequest> gradeRequestQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    @Bean
    public Queue<RunResult> runResultQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}
