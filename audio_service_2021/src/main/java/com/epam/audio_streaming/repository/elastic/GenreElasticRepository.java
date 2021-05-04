package com.epam.audio_streaming.repository.elastic;

import com.epam.audio_streaming.model.elasticsearch.GenreSearch;

public interface GenreElasticRepository extends GenericElasticRepository<GenreSearch, Long> {

//    List<GenreSearch> findByNameContaining(String nameGenre);
//
//    List<GenreSearch> findByNameIn(Collection<String> names);

}