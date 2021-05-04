package com.epam.audio_streaming.repository.elastic;

import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;

import java.util.List;

public interface ArtistElasticRepository extends GenericElasticRepository<ArtistSearch, Long> {

//    List<ArtistSearch> findByNameContaining(String nameArtist);

  List<ArtistSearch> findByNameContainingAndGenresNameContaining(String name, String genre);


}