package com.epam.audio_streaming.service.elastic.impl;

import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.repository.elastic.GenreElasticRepository;
import com.epam.audio_streaming.service.elastic.GenreESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreESServiceImpl implements GenreESService {

    @Autowired
    private GenreElasticRepository genreElasticRepository;

    @Override
    public void save(GenreSearch genreSearch) {
        genreElasticRepository.save(genreSearch);
    }

    @Override
    public void deleteById(Long id) {
        genreElasticRepository.deleteById(id);
    }
}
