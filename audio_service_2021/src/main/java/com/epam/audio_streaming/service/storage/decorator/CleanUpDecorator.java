package com.epam.audio_streaming.service.storage.decorator;

import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

@Slf4j
public class CleanUpDecorator extends StorageSourceDecorator {

    @Autowired
    private SourceService sourceService;

    public CleanUpDecorator(StorageSourceService service, SourceService sourceService) {
        super(service);
        this.sourceService = sourceService;
    }

    @Override
    public Source save(Resource resource, String name, String typeContent) throws Exception {
        Source source = super.save(resource, name, typeContent);

        if (super.exist(source) && sourceService.findByChecksum(DigestUtils.md5Hex(resource.getInputStream())) == null) {
            super.delete(source);
            throw new ValidationException("Exception in clean up decorator");
        }
        return source;
    }
}
