package pl.mimuw.zpp.quantumai.backendui.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.backendui.model.SolutionFile;
import pl.mimuw.zpp.quantumai.backendui.repository.FileRepository;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final RandomNameGenerator randomNameGenerator;

    public String createFile(SolutionFile file) {
        String id = randomNameGenerator.generateName();
        fileRepository.save(file.withId(id));
        return id;
    }

    public String createFileFromMultipartFile(MultipartFile file) throws IOException{
        String id = randomNameGenerator.generateName();
        fileRepository.save(
                SolutionFile.builder()
                                    .id(id)
                                    .data(file.getBytes())
                                    .build());
        return id;
    }

    public List<SolutionFile> getFiles() {
        return fileRepository.findAll();
    }

    public Optional<SolutionFile> getFile(String fileId) {
         return fileRepository.findById(fileId); 
    }
    
}
