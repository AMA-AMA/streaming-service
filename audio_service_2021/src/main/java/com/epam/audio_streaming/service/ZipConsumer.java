package com.epam.audio_streaming.service;

import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.models.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ZipConsumer {

    @Autowired
    private ZipService zipService;
    @Autowired
    private SourceService sourceService;

    @JmsListener(destination = "zip")
    public void saveZip(String message) throws Exception {
        String[] split = message.split("::");
        zipService.openZip((Source) sourceService.getByPathAndName(split[0], split[1]));
    }

}