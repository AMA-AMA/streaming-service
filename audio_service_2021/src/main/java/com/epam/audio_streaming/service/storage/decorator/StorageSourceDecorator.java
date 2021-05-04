package com.epam.audio_streaming.service.storage.decorator;

import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.storage.StorageSourceService;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public abstract class StorageSourceDecorator implements StorageSourceService {

    private StorageSourceService service;

    public StorageSourceDecorator(StorageSourceService service) {
        this.service = service;
    }

    @Override
    public InputStream get(Source source) throws Exception {
        return service.get(source);
    }

    @Override
    public Source save(Resource resource, String name, String typeContent) throws Exception {
        return service.save(resource, name, typeContent);
    }

    @Override
    public void delete(Source source) throws Exception {
        service.delete(source);
    }

    @Override
    public boolean exist(Source source) {
        return service.exist(source);
    }

}

