package com.epam.audio_streaming.service.elastic;

import com.epam.audio_streaming.model.Song;
import com.epam.audio_streaming.model.elasticsearch.SongSearch;

import java.util.Optional;

public interface SongESService {

    void convertorSaveSongToSearch(Song song);

    Optional<SongSearch> findById(Long id);

    void save(SongSearch songSearch);

    boolean deleteById(Long id);
}
