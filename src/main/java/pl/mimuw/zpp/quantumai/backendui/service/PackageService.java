package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.model.GraphPackage;
import pl.mimuw.zpp.quantumai.backendui.repository.GraphPackageRepository;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PackageService {
    private final GraphPackageRepository graphPackageRepository;
    private final RandomNameGenerator randomNameGenerator;

    public String createEmptyPackage(String name) {
        String id = randomNameGenerator.generateName();
        graphPackageRepository.save(
                GraphPackage.builder()
                        .packageId(id)
                        .name(name)
                        .graphIds(List.of())
                        .build()
        );
        return id;
    }

    public List<GraphPackage> getPackages() {
        return graphPackageRepository.findAll();
    }

    public GraphPackage getPackage(String packageId) {
        return graphPackageRepository.findById(packageId).orElseThrow(RuntimeException::new);
    }

    public void addGraphsToPackage(String packageId, List<String> graphIds) {
        GraphPackage graphPackage = graphPackageRepository.findById(packageId).orElseThrow(RuntimeException::new);
        List<String> mergedGraphIds = mergeAsSets(graphPackage.graphIds(), graphIds);
        graphPackageRepository.save(
                graphPackage.withGraphIds(mergedGraphIds)
        );
    }

    private List<String> mergeAsSets(List<String> s1, List<String> s2) {
        Set<String> s = new HashSet<>(s1);
        s.addAll(s2);
        return s.stream().toList();
    }
}
