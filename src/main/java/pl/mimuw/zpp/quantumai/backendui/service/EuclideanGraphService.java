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

    public void saveGraphWithId(EuclideanGraph graph) {
        euclideanGraphRepository.save(graph);
    }

    public List<EuclideanGraph> getAllGraphs() {
        return euclideanGraphRepository.findAll();
    }

    public Optional<EuclideanGraph> getGraphById(String graphId) {
        return euclideanGraphRepository.findById(graphId);
    }

    public List<EuclideanGraph> getGraphsByIdIn(List<String> graphIds) {
        return euclideanGraphRepository.findAllById(graphIds);
    }
}
