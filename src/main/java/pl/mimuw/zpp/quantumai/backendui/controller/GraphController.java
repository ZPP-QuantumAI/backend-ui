package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.repository.EuclideanGraphRepository;

import java.util.List;

@RestController
@RequestMapping("/graph")
@RequiredArgsConstructor
public class GraphController {
    private final EuclideanGraphRepository euclideanGraphRepository;

    @PostMapping("/")
    public ResponseEntity<Void> createGraph(@RequestBody EuclideanGraph graph) {
        euclideanGraphRepository.save(graph);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/")
    public ResponseEntity<List<EuclideanGraph>> getGraphs() {
        return ResponseEntity.ok(
                euclideanGraphRepository.findAll()
        );
    }
}
