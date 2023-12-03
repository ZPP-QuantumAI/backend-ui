package pl.mimuw.zpp.quantumai.backendui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.model.TspResult;
import pl.mimuw.zpp.quantumai.backendui.service.SolveService;

@RestController
@RequestMapping("/solve")
@RequiredArgsConstructor
public class SolveController {
    private final SolveService solveService;
    private final ObjectMapper mapper;

    @PostMapping("/tsp")
    public ResponseEntity<String> solveTsp(
            @RequestBody String pythonCode,
            @RequestBody String graphName
    ) throws Exception {
        Either<String, TspResult> result = solveService.solveTsp(pythonCode, graphName);
        if (result.isRight()) {
            return ResponseEntity.ok(mapper.writeValueAsString(result.get()));
        } else {
            return ResponseEntity.badRequest().body(result.getLeft());
        }
    }
}
