package com.epam.audio_streaming.service.models;

import com.epam.audio_streaming.dto.AlbumDTO;
import com.epam.audio_streaming.model.Album;
import com.epam.audio_streaming.model.elasticsearch.AlbumSearch;
import com.epam.audio_streaming.model.elasticsearch.request.AlbumSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AlbumService {

    Long create(AlbumDTO album);

    Album save(Album album);

    void delete(Album album);

    List<Long> deleteAlbum(List<String> id);

    Album findByName(String name);

    AlbumDTO getAlbum(Long id);

    void updateAlbum(AlbumDTO albumDTO, Long id);

    Album saveOnlyDB(Album album);

    boolean existsById(Long idAlbum);

    Page<AlbumSearch> searchAlbum(AlbumSearchRequest album);
}


