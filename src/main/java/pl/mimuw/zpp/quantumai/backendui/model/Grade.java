package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Builder
@Document
@With
public record Grade(
    @Id String gradeId,
    GradeStatus status,
    String graphId,
    String solutionId,
    Problem problem,
    Long runtimeInMs,
    Instant timestamp,
    Object result
) {
    public enum GradeStatus {
        WAITING,
        SUCCESS,
        FAILED
    }
}