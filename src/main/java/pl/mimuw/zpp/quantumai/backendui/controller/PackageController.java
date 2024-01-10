package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.model.GraphPackage;
import pl.mimuw.zpp.quantumai.backendui.service.PackageService;

import java.util.List;

@RestController
@RequestMapping("/package")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;

    @PostMapping("/new")
    public ResponseEntity<String> createNewPackage(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(
                packageService.createEmptyPackage(name)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<GraphPackage>> getPackages() {
        return ResponseEntity.ok(
                packageService.getPackages()
        );
    }

    @GetMapping("/")
    public ResponseEntity<GraphPackage> getPackage(
            @RequestParam String packageId
    ) {
        return ResponseEntity.ok(
                packageService.getPackage(packageId)
        );
    }

    @PostMapping("/add")
    public ResponseEntity<Void> getPackage(
            @RequestParam String packageId,
            @RequestBody List<String> graphIds) {
        packageService.addGraphsToPackage(packageId, graphIds);
        return ResponseEntity.ok(null);
    }
}
