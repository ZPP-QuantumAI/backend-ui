package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mimuw.zpp.quantumai.backendui.error.PackageNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.model.GraphPackage;
import pl.mimuw.zpp.quantumai.backendui.service.PackageService;

import java.util.List;

@RestController
@RequestMapping("/package")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;

    @PostMapping("/create")
    public ResponseEntity<String> createPackage(
        @RequestBody GraphPackage graphPackage
    ) {
        String id = packageService.createPackage(graphPackage);
        return ResponseEntity.ok(id);
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
                packageService.getGraphPackage(packageId).orElseThrow(() -> new PackageNotFoundException(packageId))
        );
    }

    @DeleteMapping("/")
    public ResponseEntity<Boolean> deletePackage(
            @RequestParam String packageId
    ) {
        return ResponseEntity.ok(
                packageService.deletePackage(packageId)
        );
    }
}
