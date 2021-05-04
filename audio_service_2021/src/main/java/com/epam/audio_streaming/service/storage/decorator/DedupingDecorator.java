package com.epam.audio_streaming.service.storage.decorator;


import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageSourceService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.Resource;

public class DedupingDecorator extends StorageSourceDecorator {

    private SourceService sourceService;

    public DedupingDecorator(StorageSourceService service, SourceService sourceService) {
        super(service);
        this.sourceService = sourceService;
    }

    @Override
    public Source save(Resource resource, String name, String typeContent) throws Exception {

            if (sourceService.findByChecksum(DigestUtils.md5Hex(resource.getInputStream())) == null) {
                return super.save(resource, name, typeContent);
            } else {
                throw new ValidationException("Deduping " + name);
            }
    }
}


