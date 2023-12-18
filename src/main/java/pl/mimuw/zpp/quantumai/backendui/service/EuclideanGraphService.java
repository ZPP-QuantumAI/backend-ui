package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.repository.EuclideanGraphRepository;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EuclideanGraphService {
    private final EuclideanGraphRepository euclideanGraphRepository;
    private final RandomNameGenerator randomNameGenerator;

    public String createGraph(EuclideanGraph graph) {
        String id = randomNameGenerator.generateName();
        euclideanGraphRepository.save(graph.withId(id));
        return id;
    }

    public List<EuclideanGraph> getGraphs() {
        return euclideanGraphRepository.findAll();
    }

    public Optional<EuclideanGraph> getGraph(String graphId) { return euclideanGraphRepository.findById(graphId); }
}
