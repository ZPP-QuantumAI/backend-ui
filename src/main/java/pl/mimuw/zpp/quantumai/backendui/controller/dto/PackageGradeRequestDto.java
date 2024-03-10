package pl.mimuw.zpp.quantumai.backendui.controller.dto;

import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;

public record PackageGradeRequestDto(
        String packageId,
        Problem problem,
        String name,
        MultipartFile solution
) {
}
