package com.epam.audio_streaming.service.storage;


import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.model.StorageTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class StorageFactory {

    private Map<StorageTypes, StorageSourceService> storages = new HashMap<>();

    public void getStorage(StorageTypes storageType, StorageSourceService storageSourceService) {

        storages.put(storageType, storageSourceService);
    }

    public Source save(Resource resource, String name, String typeContent) throws Exception {
        if (typeContent.equals("application/x-zip-compressed")) {
            return storages.get(StorageTypes.valueOf("FILE_SYSTEM")).save(resource, name, typeContent);
        }
        if (typeContent.equals("audio/mpeg")) {
            return storages.get(StorageTypes.valueOf("FILE_SYSTEM")).save(resource, name, typeContent);
//            return storages.get(StorageTypes.valueOf("AMAZON_S3")).save(resource, name, typeContent);
        }
        return null;
    }

    public InputStream get(Source source) throws Exception {
        return storages.get(source.getStorageTypes()).get(source);
    }

    public void delete(Source source) throws Exception {
        storages.get(source.getStorageTypes()).delete(source);
    }

}






