package com.epam.audio_streaming.service.storage.decorator;

import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageDecoratorFactory {

    @Autowired
    private SourceService sourceService;

    public StorageSourceService decoratorOrder(StorageSourceService storageSourceService, Class<?> classDecorator) {

        if (IoRetryDecorator.class.equals(classDecorator)) {
            return new IoRetryDecorator(storageSourceService);
        } else if (DedupingDecorator.class.equals(classDecorator)) {
            return new DedupingDecorator(storageSourceService, sourceService);
        } else if (DBDecorator.class.equals(classDecorator)) {
            return new DBDecorator(storageSourceService, sourceService);
        } else if (CleanUpDecorator.class.equals(classDecorator)) {
            return new CleanUpDecorator(storageSourceService, sourceService);
        }
        return storageSourceService;
    }

}
