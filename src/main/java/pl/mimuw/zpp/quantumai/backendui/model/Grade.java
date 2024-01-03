package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
@With
public record Grade(
    @Id String gradeId,
    GradeStatus status,
    String graphId,
    Problem problem,
    Long runtimeInMs,
    Object result
) {
    public enum GradeStatus {
        WAITING,
        SUCCESS,
        FAILED
    }
}