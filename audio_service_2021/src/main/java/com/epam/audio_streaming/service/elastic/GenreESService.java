package com.epam.audio_streaming.service.elastic;

import com.epam.audio_streaming.model.elasticsearch.GenreSearch;

public interface GenreESService {

    void save(GenreSearch genreSearch);

    void deleteById(Long id);
}


