package com.epam.audio_streaming.service.models;

import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.Source;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SourceService<T extends Source> {

    T save(Source source) throws IOException;

    Source findByName(String name);

    void delete(T source);

    void deleteById(Long id);

    boolean existById(T source);

    T get(T source);

    T findByChecksum(String checksum);

    List<T> findAll();

    Source getOne(Long id);

    void sendSource(T source) throws Exception;

    T getByPathAndName(String path, String name);

    void sendZip(Source source) throws Exception;

    void sendSongFromZip(Source source) throws Exception;

    String saveZip(MultipartFile multipartFile) throws ValidationException;
}


