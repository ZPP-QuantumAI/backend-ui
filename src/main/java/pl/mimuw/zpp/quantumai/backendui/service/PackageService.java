package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public String createPackage(GraphPackage graphPackage) {
        String id = randomNameGenerator.generateName();
        graphPackageRepository.save(graphPackage.withPackageId(id));
        return id;
    }

    public List<GraphPackage> getPackages() {
        return graphPackageRepository.findAll();
    }

    public Optional<GraphPackage> getGraphPackage(String packageId) {
        return graphPackageRepository.findById(packageId);
    }
}
