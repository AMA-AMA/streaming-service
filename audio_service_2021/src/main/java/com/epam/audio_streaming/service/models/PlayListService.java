package com.epam.audio_streaming.service.models;

import com.epam.audio_streaming.dto.PlayListDTO;
import com.epam.audio_streaming.exception.NotFoundException;
import com.epam.audio_streaming.model.PlayList;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface PlayListService {

    PlayList save(PlayList playList);

    PlayList getOne(Long id);

    Boolean createPlayList(String name, Authentication authentication);

    Page<PlayListDTO> getAllPlayLists(Authentication authentication) throws NotFoundException;

    PlayListDTO getPlayList(Long id, Authentication authentication) throws NotFoundException;

    Boolean deletePlayList(Long id, Authentication authentication) throws NotFoundException;

    void delete(PlayList playList);

    boolean existsById(Long idPlayList);
}


