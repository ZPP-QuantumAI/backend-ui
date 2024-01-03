package pl.mimuw.zpp.quantumai.backendui.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface Storage {
    String save(MultipartFile multipartFile, String path) throws IOException;

    File get(String path);
}
