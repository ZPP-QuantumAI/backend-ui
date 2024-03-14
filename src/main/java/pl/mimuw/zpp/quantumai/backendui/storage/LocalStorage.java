package pl.mimuw.zpp.quantumai.backendui.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
@ConditionalOnMissingBean(MongoStorage.class)
public class LocalStorage implements Storage {
    @Override
    public String save(MultipartFile multipartFile) throws IOException {
        File directory = Files.createTempDirectory(null).toFile();
        File file = File.createTempFile("solve", null, directory);
        multipartFile.transferTo(file);
        return file.getAbsolutePath();
    }

    @Override
    public File get(String path) throws IOException {
        return new File(path);
    }
}
