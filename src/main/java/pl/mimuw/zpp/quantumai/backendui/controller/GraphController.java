package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.GraphDto;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.MapGraphCreationRequestDto;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.service.EuclideanGraphService;
import pl.mimuw.zpp.quantumai.backendui.service.GraphService;
import pl.mimuw.zpp.quantumai.backendui.service.MapGraphService;

import java.util.List;

@RestController
@RequestMapping("/graph")
@RequiredArgsConstructor
public class GraphController {
    private final EuclideanGraphService euclideanGraphService;
    private final MapGraphService mapGraphService;
    private final GraphService graphService;

    @PostMapping("/")
    public ResponseEntity<String> createGraph(@RequestBody EuclideanGraph graph) {
        return ResponseEntity.ok(
                euclideanGraphService.createGraph(graph)
        );
    }

    @PostMapping("/map")
    public ResponseEntity<String> createMapGraph(@RequestBody MapGraphCreationRequestDto request) {
        return ResponseEntity.ok(
                mapGraphService.createGraph(request)
        );
    }

    @Deprecated
    @GetMapping("/all")
    public ResponseEntity<List<EuclideanGraph>> getGraphs() {
        return ResponseEntity.ok(
                euclideanGraphService.getAllGraphs()
        );
    }

    @GetMapping("/all/new")
    public ResponseEntity<List<GraphDto>> getGraphsOld() {
        return ResponseEntity.ok(
                graphService.getAllGraphs()
        );
    }

    @Deprecated
    @GetMapping("/")
    public ResponseEntity<EuclideanGraph> getGraphOld(@RequestParam String graphId) {
        return euclideanGraphService.getGraphById(graphId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/new")
    public ResponseEntity<GraphDto> getGraph(@RequestParam String graphId) {
        return graphService.getGraphById(graphId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/")
    public ResponseEntity<Boolean> deleteGraph(@RequestParam String graphId) {
        return ResponseEntity.ok(
                graphService.deleteGraph(graphId)
        );
    }
}
