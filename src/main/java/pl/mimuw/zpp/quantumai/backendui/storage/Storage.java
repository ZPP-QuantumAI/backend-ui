package pl.mimuw.zpp.quantumai.backendui.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface Storage {
    String save(MultipartFile multipartFile) throws IOException;
}
