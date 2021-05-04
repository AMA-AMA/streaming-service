package com.epam.audio_streaming.service.elastic;

import com.epam.audio_streaming.model.Album;
import com.epam.audio_streaming.model.elasticsearch.AlbumSearch;
import com.epam.audio_streaming.model.elasticsearch.request.AlbumSearchRequest;
import org.springframework.data.domain.Page;

public interface AlbumESService {

    void deleteById(Long id);

    void save(AlbumSearch albumSearch);

    void convertorAndSaveAlbum(Album album);

    Page<AlbumSearch> searchAlbum(AlbumSearchRequest album);
}
