package pl.mimuw.zpp.quantumai.backendui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

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
            @RequestBody SolveInput solveInput
    ) throws Exception {
        Either<String, TspResult> result = solveService.solveTsp(solveInput.pythonCode(), solveInput.graphName());
        if (result.isRight()) {
            return ResponseEntity.ok(mapper.writeValueAsString(result.get()));
        } else {
            return ResponseEntity.badRequest().body(result.getLeft());
        }
    }

    // Similar to the above, but the input is a file
    @PostMapping("/tsp/file")
    public ResponseEntity<String> solveTspWithFile(
            @ModelAttribute SolveInputFile solveInputFile
    ) throws Exception {
        String pythonCode = new String(solveInputFile.pythonFile().getBytes(), StandardCharsets.UTF_8);
        Either<String, TspResult> result = solveService.solveTsp(pythonCode, solveInputFile.graphName());
        if (result.isRight()) {
            return ResponseEntity.ok(mapper.writeValueAsString(result.get()));
        } else {
            return ResponseEntity.badRequest().body(result.getLeft());
        }
    }

    record SolveInput(
       String pythonCode,
       String graphName
    ) {}

    record SolveInputFile(
        MultipartFile pythonFile,
        String graphName
    ) {}

}