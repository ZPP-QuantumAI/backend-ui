package pl.mimuw.zpp.quantumai.backendui.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class MongoStorage implements Storage {
    private final GridFsTemplate gridFsTemplate;
    @Override
    public String save(MultipartFile multipartFile) throws IOException {
        return gridFsTemplate.store(multipartFile.getInputStream(), null, "application/zip").toString();
    }
}
