package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.AlbumDTO;
import com.epam.audio_streaming.model.elasticsearch.AlbumSearch;
import com.epam.audio_streaming.model.elasticsearch.request.AlbumSearchRequest;
import com.epam.audio_streaming.service.elastic.AlbumESService;
import com.epam.audio_streaming.service.models.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/create-album")
    public Long createAlbum(@RequestBody @Valid AlbumDTO album) {
        return albumService.create(album);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/update-album/{id}")
    public void updateAlbum(@PathVariable Long id, @RequestBody @Valid AlbumDTO albumDTO) {
        albumService.updateAlbum(albumDTO, id);
    }

    //    @PreAuthorize("hasAuthority('ROLE_USER)")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping(value = "/get-album/{id}")
    public AlbumDTO getAlbum(@PathVariable Long id) {
        return albumService.getAlbum(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete-album/{ids}")
    public List<Long> deleteAlbum(@PathVariable List<String> ids) {
        return albumService.deleteAlbum(ids);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/search-album")
    public Page<AlbumSearch> searchAlbum(@RequestBody @Valid AlbumSearchRequest album) {
        return albumService.searchAlbum(album);
    }
}
