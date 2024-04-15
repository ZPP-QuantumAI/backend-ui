package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.SolutionDto;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;
import pl.mimuw.zpp.quantumai.backendui.service.SolutionService;

import java.util.List;

@RestController
@RequestMapping("/solution")
@RequiredArgsConstructor
public class SolutionController {
    private final SolutionService solutionService;

    @GetMapping("/")
    public ResponseEntity<List<SolutionDto>> getSolutionsForProblem(
            @RequestParam Problem problem
    ) {
        return ResponseEntity.ok(
                solutionService.getAllByProblem(problem)
        );
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteSolution(
            @RequestParam String solutionId
    ) {
        solutionService.deleteSolution(solutionId);
        return ResponseEntity.ok(null);
    }
}
