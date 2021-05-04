package com.epam.audio_streaming.service.models;

import com.epam.audio_streaming.dto.GenreDTO;
import com.epam.audio_streaming.exception.NotFoundException;
import com.epam.audio_streaming.model.Genre;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenreService {

    void create(Genre genre);

    Genre saveOnlyDB(Genre genre);

    Page<Genre> readAll();

    Page<GenreDTO> readAllGenre() throws NotFoundException;

    Genre findByName(String name);

    Long createGenre(GenreDTO genre);

    Genre save(Genre genre);

    void delete(Genre genre);

    List<Long> deleteGenres(List<String> ids);

    Genre getOne(Long id);

    boolean existsById(Long idGenre);
}


