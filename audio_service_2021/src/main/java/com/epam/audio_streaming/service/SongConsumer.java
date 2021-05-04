package com.epam.audio_streaming.service;

import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class SongConsumer {

    @Autowired
    private SongInfoService songInfoService;
    @Autowired
    private StorageFactory storageFactory;
    @Autowired
    private SourceService sourceService;

    @JmsListener(destination = "song")
    public void saveSong(String message) throws Exception {
        String[] split = message.split("::");
        Source source = sourceService.getByPathAndName(split[0], split[1]);
        InputStream inputStream = storageFactory.get(source);
        songInfoService.saveAudio(inputStream, source);

    }

}