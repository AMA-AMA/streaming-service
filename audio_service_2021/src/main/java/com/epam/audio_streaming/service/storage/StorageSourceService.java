package com.epam.audio_streaming.service.storage;

import com.epam.audio_streaming.annotation.Decorate;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.storage.decorator.CleanUpDecorator;
import com.epam.audio_streaming.service.storage.decorator.DBDecorator;
import com.epam.audio_streaming.service.storage.decorator.DedupingDecorator;
import com.epam.audio_streaming.service.storage.decorator.IoRetryDecorator;
import org.springframework.core.io.Resource;

import java.io.InputStream;

@Decorate(decorators = {IoRetryDecorator.class, DBDecorator.class, DedupingDecorator.class, CleanUpDecorator.class})
public interface StorageSourceService {

    InputStream get(Source source) throws Exception;

    Source save(Resource resource, String name, String typeContent) throws Exception;

    void delete(Source source) throws Exception;

    boolean exist(Source source);

}


