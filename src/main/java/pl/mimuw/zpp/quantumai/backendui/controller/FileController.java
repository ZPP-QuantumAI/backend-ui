package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import pl.mimuw.zpp.quantumai.backendui.model.SolutionFile;
import pl.mimuw.zpp.quantumai.backendui.service.FileService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createFile(@RequestBody MultipartFile file) {
        try {
            String fileId = fileService.createFileFromMultipartFile(file);
            return ResponseEntity.ok(fileId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot convert MultiPartFile to byte[]");
        }
    }

    @GetMapping("/")
    public ResponseEntity<SolutionFile> getFile(@RequestBody String id) {
        Optional<SolutionFile> optionalFile = fileService.getFile(id);
        if (optionalFile.isPresent()) {
            return ResponseEntity.ok(optionalFile.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SolutionFile>> getFiles() {
        return ResponseEntity.ok(
                fileService.getFiles()
        );
    }
    
}
