package pl.mimuw.zpp.quantumai.backendui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.model.TspResult;
import pl.mimuw.zpp.quantumai.backendui.service.SolveService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

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
    @PostMapping(value = "/tsp/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> solveTspWithFile(
        @RequestPart("pythonFile") MultipartFile pythonFile,
        @RequestPart("graphName") String graphName
    ) throws Exception {
        String pythonCode = new String(pythonFile.getBytes(), StandardCharsets.UTF_8);
        Either<String, TspResult> result = solveService.solveTsp(pythonCode, graphName);
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

}