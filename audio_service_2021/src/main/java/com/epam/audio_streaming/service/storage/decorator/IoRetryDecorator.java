package com.epam.audio_streaming.service.storage.decorator;

import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.service.storage.StorageSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Slf4j
public class IoRetryDecorator extends StorageSourceDecorator {

    public IoRetryDecorator(StorageSourceService service) {
        super(service);
    }

    @Override
    public Source save(Resource resource, String name, String typeContent) throws Exception {
        int count = 0;
        int maxTries = 3;
        while (true) {
            try {
                return super.save(resource, name, typeContent);
            } catch (IOException e) {
                if (++count == maxTries) {
                    throw new ValidationException("IOException file " + name + " when trying to save");
                }
            }
        }
    }

    @Override
    public void delete(Source source) throws Exception {
        int count = 0;
        int maxTries = 3;
        while (true) {
            try {
                super.delete(source);
                return;
            } catch (IOException e) {
                if (++count == maxTries) {
                    throw new ValidationException("IOException file when trying to delete ");
                }
            }
        }
    }

}
