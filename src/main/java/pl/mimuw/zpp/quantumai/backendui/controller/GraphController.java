package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.service.EuclideanGraphService;

import java.util.List;

@RestController
@RequestMapping("/graph")
@RequiredArgsConstructor
public class GraphController {
    private final EuclideanGraphService euclideanGraphService;

    @PostMapping("/")
    public ResponseEntity<String> createGraph(@RequestBody EuclideanGraph graph) {
        return ResponseEntity.ok(
                euclideanGraphService.createGraph(graph)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<EuclideanGraph>> getGraphs() {
        return ResponseEntity.ok(
                euclideanGraphService.getGraphs()
        );
    }

    @GetMapping("/")
    public ResponseEntity<EuclideanGraph> getGraph(@RequestBody String graphId) {
        return euclideanGraphService.getGraph(graphId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
