package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mimuw.zpp.quantumai.backendui.feign.UtmPythonFeignClient;
import pl.mimuw.zpp.quantumai.backendui.repository.TestEntry;
import pl.mimuw.zpp.quantumai.backendui.repository.TestEntryRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final UtmPythonFeignClient utmPythonFeignClient;
    private final TestEntryRepository testEntryRepository;

    @GetMapping("/hello-world")
    public String test() {
        return utmPythonFeignClient.test();
    }

    @PostMapping("/entry")
    public ResponseEntity<Void> saveEntry(@RequestParam String entry) {
        testEntryRepository.save(
                TestEntry.builder()
                        .entry(entry)
                        .build()
        );
        return ResponseEntity.ok(null);
    }

    @GetMapping("/entry")
    public ResponseEntity<List<TestEntry>> getEntries() {
        return ResponseEntity.ok(
                testEntryRepository.findAll()
        );
    }
}
