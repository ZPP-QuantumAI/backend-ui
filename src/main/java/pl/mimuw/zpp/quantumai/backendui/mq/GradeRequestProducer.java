package pl.mimuw.zpp.quantumai.backendui.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.mimuw.zpp.quantumai.backendui.model.GradeRequest;

import java.util.Queue;

@Component
@RequiredArgsConstructor
public class GradeRequestProducer {
    private final Queue<GradeRequest> gradeRequestQueue;
    public void generateGradeRequest(GradeRequest request) {
        gradeRequestQueue.offer(request);
    }
}
