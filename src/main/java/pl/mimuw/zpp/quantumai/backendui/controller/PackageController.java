package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.controller.dto.GraphPackageDto;
import pl.mimuw.zpp.quantumai.backendui.error.PackageNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.service.PackageService;

import java.util.List;

@RestController
@RequestMapping("/package")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;

    @PostMapping("/create")
    public ResponseEntity<String> createPackage(
        @RequestBody PackageCreateRequest packageCreateRequest
    ) {
        String id = packageService.createPackage(
                packageCreateRequest.name(),
                packageCreateRequest.graphs()
        );
        return ResponseEntity.ok(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GraphPackageDto>> getPackages() {
        return ResponseEntity.ok(
                packageService.getAllPackageDtos()
        );
    }

    @GetMapping("/")
    public ResponseEntity<GraphPackageDto> getPackage(
            @RequestParam String packageId
    ) {
        return ResponseEntity.ok(
                packageService.getGraphPackageDto(packageId).orElseThrow(() -> new PackageNotFoundException(packageId))
        );
    }

    private record PackageCreateRequest(
        String name,
        List<EuclideanGraph> graphs
    ) {}
}
