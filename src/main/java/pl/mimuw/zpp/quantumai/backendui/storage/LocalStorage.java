package pl.mimuw.zpp.quantumai.backendui.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
@Primary
public class LocalStorage implements Storage {
    @Override
    public String save(MultipartFile multipartFile, String path) throws IOException {
        File directory = Files.createTempDirectory(path).toFile();
        File file = File.createTempFile("solve", null, directory);
        multipartFile.transferTo(file);
        return file.getAbsolutePath();
    }

    @Override
    public File get(String path) {
        return new File(path);
    }
}
