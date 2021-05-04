package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.PlayListDTO;
import com.epam.audio_streaming.service.models.PlayListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/play-list")
@RestController
public class PlayListController {

    @Autowired
    private PlayListService playListService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/save-list")
    public Boolean savePlayList(String name) throws Exception {
        return playListService.createPlayList(name, SecurityContextHolder.getContext().getAuthentication());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/get-all-list")
    public Page<PlayListDTO> getAllPlayLists() throws Exception {
        return playListService.getAllPlayLists(SecurityContextHolder.getContext().getAuthentication());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/get-list/{id}")
    public PlayListDTO getPlayList(@PathVariable Long id ) throws Exception {
        return playListService.getPlayList(id, SecurityContextHolder.getContext().getAuthentication());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/delete-list/{id}")
    public Boolean deletePlayList(@PathVariable Long id ) throws Exception {
        return playListService.deletePlayList(id, SecurityContextHolder.getContext().getAuthentication());
    }

}