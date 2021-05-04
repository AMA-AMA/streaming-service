package com.epam.audio_streaming.repository.models;

import com.epam.audio_streaming.model.Source;

public interface SourceRepository extends GenericRepository<Source, Long> {

    Source findByChecksum(String checksum);

    Source findByPathAndName(String path, String name);
}