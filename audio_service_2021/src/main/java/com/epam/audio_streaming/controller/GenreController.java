package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.GenreDTO;
import com.epam.audio_streaming.exception.NotFoundException;
import com.epam.audio_streaming.service.models.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/get-all-genres")
    public Page<GenreDTO> getGenre() throws NotFoundException {
        return genreService.readAllGenre();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/create-genre")
    public Long createGenre(@RequestBody @Valid GenreDTO genre) {
        return genreService.createGenre(genre);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete-genre/{ids}")
    public List<Long> deleteGenre(@PathVariable List<String> ids) {
        return genreService.deleteGenres(ids);
    }

}

