package com.epam.audio_streaming.service.models;

import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.Song;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SongService {

    Song save(Song song) throws IOException;

    void delete(Song song);

    Song findByName(String name);

    Song getOne(Long id);

    List<Long> deleteSongs(List<String> ids) throws Exception;

    ResponseEntity<byte[]> getSongById(Long id) throws Exception;

    Song saveOnlyDB(Song song) throws IOException;

    Boolean addSongPlayList(Long id, Long list, Authentication authentication);

    Boolean deleteSongPlayList(Long idSong, Long list, Authentication authentication) throws IOException;

    boolean existsById(Long idSong);

    String saveSong(MultipartFile multipartFile) throws ValidationException;
}


