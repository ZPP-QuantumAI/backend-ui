package pl.mimuw.zpp.quantumai.backendui.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.mimuw.zpp.quantumai.backendui.model.SolutionFile;
import pl.mimuw.zpp.quantumai.backendui.repository.FileRepository;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        value = "storage.mongo.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class MongoStorage implements Storage {
    private final RandomNameGenerator randomNameGenerator;
    private final FileRepository fileRepository;
    @Override
    public String save(MultipartFile multipartFile) throws IOException {
        String id = randomNameGenerator.generateName();
        fileRepository.save(
                SolutionFile.builder()
                        .solutionId(id)
                        .data(new Binary(multipartFile.getBytes()))
                        .build()
        );
        return id;
    }

    @Override
    public File get(String id) throws IOException {
        SolutionFile solutionFile = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("solutionFile with id = " + id + " not found"));
        File tempFile = File.createTempFile(id, ".zip");
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        fileOutputStream.write(solutionFile.data().getData());
        log.info("Successfully read file with id = {} to file = {}", id, tempFile.getAbsolutePath());
        return tempFile;
    }
}
