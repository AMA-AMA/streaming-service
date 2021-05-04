package com.epam.audio_streaming.service.models;

import com.epam.audio_streaming.dto.ArtistDTO;
import com.epam.audio_streaming.model.Artist;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.request.ArtistSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArtistService {

    Artist saveArtist(Artist artist);

    void deleteArtist(Artist artist);

    Artist saveOnlyDB(Artist artist);

    ArtistDTO getArtistById(Long id);

    boolean updateArtist(ArtistDTO artist, Long id);

    Long createArtist(ArtistDTO artist);

    Artist findByName(String name);

    List<Long> deleteArtists(List<String> id);

    Artist getOne(Long a);

    boolean existsById(Long idArtist);

    Page<ArtistSearch> searchArtist(ArtistSearchRequest artist);
}


