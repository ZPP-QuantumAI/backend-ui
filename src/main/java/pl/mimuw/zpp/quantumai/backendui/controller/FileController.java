package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.backendui.model.SolutionFile;
import pl.mimuw.zpp.quantumai.backendui.service.FileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createFile(@RequestBody MultipartFile file) throws IOException {
        String fileId = fileService.createFileFromMultipartFile(file);
        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/")
    public ResponseEntity<SolutionFile> getFile(@RequestParam String id) {
        return fileService.getFile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<SolutionFile>> getFiles() {
        return ResponseEntity.ok(
                fileService.getFiles()
        );
    }
    
}
