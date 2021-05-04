package com.epam.audio_streaming.service.storage;

import com.epam.audio_streaming.annotation.StorageType;
import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.model.StorageTypes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@StorageType(StorageTypes.FILE_SYSTEM)
public class StorageFileSystem implements StorageSourceService {

    @Value("${path.local.storage}")
    private String pathLocalStorage;


    @Override
    public InputStream get(Source source) throws Exception {
        Path path = Paths.get(source.getPath(), source.getName());
        return new FileSystemResource(path).getInputStream();
    }

    @Override
    public Source save(Resource resource, String name, String typeContent) throws Exception {

        if (!Files.exists(Paths.get(pathLocalStorage, name))) {

            Files.createDirectories(Paths.get(pathLocalStorage, name).getParent());
            Files.copy(resource.getInputStream(), Paths.get(pathLocalStorage, name));
            Source source = new Source(name, resource.contentLength(), DigestUtils.md5Hex(resource.getInputStream()),
                    pathLocalStorage, typeContent);
            source.setStorageTypes(StorageTypes.FILE_SYSTEM);
            return source;
        }
        throw new ValidationException("FL exist file with name: " + name);
    }

    @Override
    public void delete(Source source) throws Exception {
        Files.delete(Paths.get(source.getPath(), source.getName()));
    }

    @Override
    public boolean exist(Source source) {
        return Files.exists(Paths.get(source.getPath(), source.getName()));
    }

}

