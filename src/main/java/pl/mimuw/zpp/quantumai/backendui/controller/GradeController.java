package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;
import pl.mimuw.zpp.quantumai.backendui.service.GradeService;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PostMapping(value = "/generateRequest", consumes = {MULTIPART_FORM_DATA_VALUE, TEXT_PLAIN_VALUE})
    public ResponseEntity<String> generateGradeRequest(
            @RequestParam String graphId,
            @RequestParam Problem problem,
            @RequestPart MultipartFile solution
    ) throws IOException {
        return ResponseEntity.ok(
                gradeService.generateGradeRequest(graphId, problem, solution)
        );
    }

    @GetMapping(value = "/")
    public ResponseEntity<Grade> getGrade(
            @RequestParam String gradeId
    ) {
        return gradeService.getGrade(gradeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
