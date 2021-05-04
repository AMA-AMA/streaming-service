package com.epam.audio_streaming.repository.elastic;

import com.epam.audio_streaming.model.elasticsearch.SongSearch;

public interface SongElasticRepository extends GenericElasticRepository<SongSearch, Long> {

}