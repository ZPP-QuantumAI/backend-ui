package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.PackageGradeDto;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;
import pl.mimuw.zpp.quantumai.backendui.service.GradeService;
import pl.mimuw.zpp.quantumai.backendui.service.SolutionService;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;
    private final SolutionService solutionService;

    @Deprecated
    @PostMapping(value = "/graph", consumes = {MULTIPART_FORM_DATA_VALUE, TEXT_PLAIN_VALUE})
    public ResponseEntity<String> generateGradeRequestForGraph(
            @RequestParam String graphId,
            @RequestParam Problem problem,
            @RequestPart MultipartFile solution
    ) throws IOException {
        return ResponseEntity.ok(
                gradeService.generateGradeRequestForGraph(graphId, problem, solution)
        );
    }

    @PostMapping(value = "/package", consumes = {MULTIPART_FORM_DATA_VALUE, TEXT_PLAIN_VALUE})
    public ResponseEntity<String> generateGradeRequestForPackage(
            @RequestParam String packageId,
            @RequestParam Problem problem,
            @RequestPart MultipartFile solution
    ) throws IOException {
        return ResponseEntity.ok(gradeService.generateGradeRequestForPackage(packageId, problem, solution));
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
