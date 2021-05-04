package com.epam.audio_streaming.service.elastic;

import com.epam.audio_streaming.model.Artist;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.request.ArtistSearchRequest;
import org.springframework.data.domain.Page;

public interface ArtistESService {

    Page<ArtistSearch> searchArtist(ArtistSearchRequest artist);

    void convertorArtist(Artist artist);

    void deleteById(Long id);

    void save(ArtistSearch artistSearch);
}
