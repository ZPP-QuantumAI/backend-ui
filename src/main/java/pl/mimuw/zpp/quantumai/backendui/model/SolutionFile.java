package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import lombok.With;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

@Builder
@With
public record SolutionFile(
        @Id String solutionId,
        Binary data
) {}
