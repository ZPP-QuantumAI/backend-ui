package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.PackageGradeDto;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.PackageGradeRequestDto;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.service.GradeService;
import pl.mimuw.zpp.quantumai.backendui.service.SolutionService;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;
    private final SolutionService solutionService;

    @PostMapping(value = "/package", consumes = { MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> generateGradeRequestForPackage(
            @ModelAttribute PackageGradeRequestDto packageGradeRequestDto
    ) throws IOException {
        return ResponseEntity.ok(gradeService.generateGradeRequestForPackage(packageGradeRequestDto));
    }

    @GetMapping(value = "/")
    public ResponseEntity<Grade> getGrade(
            @RequestParam String gradeId
    ) {
        return gradeService.getGrade(gradeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Grade>> getGrades() {
        return ResponseEntity.ok(gradeService.getGrades());
    }

    @GetMapping(value = "/package")
    public ResponseEntity<PackageGradeDto> getGradesFromPackage(
            @RequestParam String solutionId
    ) {
        return ResponseEntity.ok(
                solutionService.getPackageGradeDto(solutionId)
        );
    }
}
