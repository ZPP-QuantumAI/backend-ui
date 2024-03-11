package pl.mimuw.zpp.quantumai.backendui.model;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
@With
public record GraphPackage(
        @Id String packageId,
        String name,
        String description,
        List<String> graphIds
) {
}
