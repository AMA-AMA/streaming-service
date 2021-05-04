package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.service.models.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongService songService;


//    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/save-songs")
    public String saveSong(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        return songService.saveSong(multipartFile);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping(value = "/get-song/{id}")
    public ResponseEntity<byte[]> getById(@PathVariable Long id) throws Exception {
        return songService.getSongById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete-song/{ids}")
    public List<Long> deleteById(@PathVariable List<String> ids) throws Exception {
        return songService.deleteSongs(ids);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping(value = "/add-song-playlist/{id}/{list}")
    public Boolean addSongPlayList(
            @PathVariable(name = "id") Long idSong,
            @PathVariable Long list
    ) throws Exception {
        return songService.addSongPlayList(idSong, list, SecurityContextHolder.getContext().getAuthentication());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping(value = "/delete-song-playlist/{id}/{list}")
    public Boolean deleteSongFromPlayList(
            @PathVariable(name = "id") Long idSong,
            @PathVariable Long list
    ) throws Exception {
        return songService.deleteSongPlayList(idSong, list, SecurityContextHolder.getContext().getAuthentication());
    }

}