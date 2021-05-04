package com.epam.audio_streaming.service.models.impl;

import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.repository.models.SourceRepository;
import com.epam.audio_streaming.service.SongInfoService;
import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service

public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private StorageFactory storageFactory;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private SongInfoService songInfoService;

    @Override
    public Source save(Source source) throws IOException {
        return sourceRepository.save(source);
    }

    @Override
    public void delete(Source source) {
        sourceRepository.delete(source);
    }

    @Override
    public void deleteById(Long id) {
        sourceRepository.deleteById(id);
    }

    @Override
    public boolean existById(Source source) {
        return sourceRepository.existsById(source.getId());
    }

    @Override
    public Source get(Source source) {
        return sourceRepository.findByName(source.getName());
    }

    @Override
    public Source findByName(String name) {
        return sourceRepository.findByName(name);
    }

    @Override
    public Source getByPathAndName(String path, String name) {
        return sourceRepository.findByPathAndName(path, name);
    }

    @Override
    public Source findByChecksum(String checksum) {
        return sourceRepository.findByChecksum(checksum);
    }

    @Override
    public List<Source> findAll() {
        return sourceRepository.findAll();
    }

    @Override
    public Source getOne(Long id) {
        return sourceRepository.getOne(id);
    }

    @Override
    @Transactional
    public void sendSource(Source source) throws Exception {

        try {
            if (source.getFileTypes().equals("audio/mpeg")) {
                sendSongFromZip(source);
            }
            if (source.getFileTypes().equals("application/x-zip-compressed")) {
                sendZip(source);
            }
        } catch (Exception e) {
            log.error("Exception " + e);
            storageFactory.delete(source);
        }
    }

    @Override
    public void sendZip(Source source) throws Exception {
        String path = source.getPath() + "::" + source.getName();
        jmsTemplate.convertAndSend("zip", path);
    }

    @Override
    public void sendSongFromZip(Source source) throws Exception {
        String message = source.getPath() + "::" + source.getName();
        jmsTemplate.convertAndSend("song", message);
    }


    @Override
    public String saveZip(MultipartFile multipartFile) throws ValidationException {
        try {
            sendSource(storageFactory.save(multipartFile.getResource(), multipartFile.getOriginalFilename(),
                    multipartFile.getContentType()));
        } catch (Exception e) {
            throw new ValidationException("Exception save zip");
        }
        return "OK";
    }
}


