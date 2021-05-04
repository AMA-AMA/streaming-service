package com.epam.audio_streaming.service.impl;

import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.SongInfoService;
import com.epam.audio_streaming.service.ZipService;
import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class ZipServiceImpl implements ZipService {

    @Autowired
    private StorageFactory storageFactory;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private SongInfoService songInfoService;
    @Autowired
    private SourceService sourceService;

    @Override
    public void openZip(Source zip) throws Exception {
        InputStream inputStream = storageFactory.get(zip);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                String name = FilenameUtils.getName(zipEntry.getName());
                Resource resource = new ByteArrayResource(IOUtils.toByteArray(zipInputStream));
                String typeContent = getTypeContent(name, resource);
                if (typeContent.equals("audio/mpeg")) {
                    Source save = storageFactory.save(resource, name,typeContent);
                    sourceService.sendSource(save);
                }
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
        storageFactory.delete(zip);
    }

    private String getTypeContent(String name, Resource resource) throws IOException, TikaException, SAXException {
        BufferedInputStream inputStream = new BufferedInputStream(resource.getInputStream());
        Metadata metadata = new Metadata();
        new AutoDetectParser().parse(inputStream, new DefaultHandler(), metadata, new ParseContext());
        inputStream.close();
        return metadata.get(Metadata.CONTENT_TYPE);

    }

}


