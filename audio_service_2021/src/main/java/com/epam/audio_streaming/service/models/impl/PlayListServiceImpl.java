package com.epam.audio_streaming.service.models.impl;

import com.epam.audio_streaming.dto.PlayListDTO;
import com.epam.audio_streaming.dto.SongDTO;
import com.epam.audio_streaming.exception.NotFoundException;
import com.epam.audio_streaming.model.PlayList;
import com.epam.audio_streaming.model.User;
import com.epam.audio_streaming.repository.models.PlayListRepository;
import com.epam.audio_streaming.service.models.PlayListService;
import com.epam.audio_streaming.service.models.SongService;
import com.epam.audio_streaming.service.models.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PlayListServiceImpl implements PlayListService {

    @Autowired
    private PlayListRepository playListRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SongService songService;

    @Override
    public PlayList save(PlayList playList) {
        return playListRepository.save(playList);
    }

    @Override
    public PlayList getOne(Long id) {
        return playListRepository.getOne(id);
    }

    @Override
    @Transactional
    public Boolean createPlayList(String name, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());

        if (playListRepository.findByName(name) == null) {
            PlayList list = new PlayList(name);

            user.getPlayLists().add(list);
            userService.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Page<PlayListDTO> getAllPlayLists(Authentication authentication) throws NotFoundException {

        List<PlayList> playLists = userService.findByEmail(authentication.getName()).getPlayLists();
        List<PlayListDTO> playListDTO = new ArrayList<>();

        if (playLists != null) {
            playLists.forEach(p -> playListDTO.add(convertPlayListDTO(p)));
            return new PageImpl<>(playListDTO, PageRequest.of(0, 3), playListDTO.size());
        }
        throw new NotFoundException("Not found play list");
    }

    @Override
    public PlayListDTO getPlayList(Long id, Authentication authentication) throws NotFoundException {

        PlayList playList = getList(id, authentication);
        if (playList != null) {
            return convertPlayListDTO(playList);
        }
        throw new NotFoundException("Not found play list with id " + id);
    }


    @Override
    @Transactional
    public Boolean deletePlayList(Long id, Authentication authentication) throws NotFoundException {
        PlayList playList = getList(id, authentication);
        if (playList != null) {
            playList.getSongs().forEach(a -> {
                try {
                    a.getPlayLists().remove(playList);
                    songService.save(a);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            User user = userService.findByEmail(authentication.getName());
            user.getPlayLists().remove(playList);
            userService.save(user);
            delete(playList);
            return true;
        }
        throw new NotFoundException("Not found play list with id " + id);

    }

    @Override
    @Transactional
    public void delete(PlayList playList) {
        playListRepository.delete(playList);
    }

    @Override
    public boolean existsById(Long idPlayList) {
        return playListRepository.existsById(idPlayList);
    }

    private PlayListDTO convertPlayListDTO(PlayList playList) {
        PlayListDTO playListDTO = new PlayListDTO(playList.getId(),playList.getName());
        List<SongDTO> songDTOList = new ArrayList<>();
        if (playList.getSongs() != null) {
            playList.getSongs().forEach(song -> {
                songDTOList.add(new SongDTO(song.getId(), song.getName(), song.getNotes(), song.getYear()));
            });

            playListDTO.setSongs(songDTOList);
        }

        return playListDTO;
    }

    private PlayList getList(Long id, Authentication authentication) {
        return userService.findByEmail(authentication.getName()).getPlayLists().stream()
                .filter(a -> Objects.equals(a.getId(), id))
                .findFirst()
                .get();
    }

}

