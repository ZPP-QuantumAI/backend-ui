package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.GraphPackageDto;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.GraphPackage;
import pl.mimuw.zpp.quantumai.backendui.repository.GraphPackageRepository;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackageService {
    private final GraphPackageRepository graphPackageRepository;
    private final RandomNameGenerator randomNameGenerator;
    private final EuclideanGraphService euclideanGraphService;

    public String createPackage(
            String name,
            List<EuclideanGraph> graphs
    ) {
        String id = randomNameGenerator.generateName();
        List<String> graphIds = graphs.stream()
                        .map(euclideanGraphService::createGraph)
                        .toList();
        graphPackageRepository.save(
                GraphPackage.builder()
                        .packageId(id)
                        .name(name)
                        .graphIds(graphIds)
                        .build()
        );
        return id;
    }

    public List<GraphPackageDto> getAllPackageDtos() {
        return graphPackageRepository.findAll().stream()
                .map(GraphPackage::packageId)
                .map(this::getGraphPackageDto)
                .flatMap(Optional::stream)
                .toList();
    }

    public Optional<GraphPackageDto> getGraphPackageDto(String packageId) {
        return graphPackageRepository.findById(packageId).map(
                graphPackage ->
                        GraphPackageDto.builder()
                                .packageId(packageId)
                                .name(graphPackage.name())
                                .graphs(euclideanGraphService.getGraphs(graphPackage.graphIds()))
                                .build()
        );
    }
}
