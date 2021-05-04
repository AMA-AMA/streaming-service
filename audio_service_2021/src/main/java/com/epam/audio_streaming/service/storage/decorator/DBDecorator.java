package com.epam.audio_streaming.service.storage.decorator;

import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.models.SongService;
import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import javax.persistence.OptimisticLockException;


@Slf4j
public class DBDecorator extends StorageSourceDecorator {

    private SourceService sourceService;
    private SongService songService;

    public DBDecorator(StorageSourceService service, SourceService sourceService) {
        super(service);
        this.sourceService = sourceService;
    }

    @Override
    public Source save(Resource resource, String name, String typeContent) throws Exception {
        try {
            return sourceService.save(super.save(resource, name, typeContent));
        } catch (OptimisticLockException e) {
            throw new ValidationException("Exception save file " + name, e);
        }
    }

    @Override
    public void delete(Source source) throws Exception {
        sourceService.delete(source);
        super.delete(source);
    }

    @Override
    public boolean exist(Source source) {
        if (sourceService.existById(source)) {
            return super.exist(source);
        } else {
            return false;
        }
    }

}

