package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.ArtistDTO;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.request.ArtistSearchRequest;
import com.epam.audio_streaming.service.elastic.ArtistESService;
import com.epam.audio_streaming.service.models.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/create-artist")
    public Long createArtist(@RequestBody @Valid ArtistDTO artist) {
        return artistService.createArtist(artist);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/update-artist/{id}")
    public boolean updateArtist(@PathVariable Long id,@RequestBody @Valid ArtistDTO artist) {
        return artistService.updateArtist(artist, id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping(value = "/get-artist/{id}")
    public ArtistDTO getArtist(@PathVariable Long id) {
        return artistService.getArtistById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "delete-artist/{ids}")
    public List<Long> deleteArtist(@PathVariable List<String> ids) {
        return artistService.deleteArtists(ids);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/search-artist")
    public Page<ArtistSearch> searchArtist(@RequestBody @Valid ArtistSearchRequest artist) {
        return artistService.searchArtist(artist);
    }
}