package com.epam.audio_streaming.service.models.impl;

import com.epam.audio_streaming.exception.ValidationException;
import com.epam.audio_streaming.model.PlayList;
import com.epam.audio_streaming.model.Song;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.model.User;
import com.epam.audio_streaming.repository.models.SongRepository;
import com.epam.audio_streaming.service.elastic.SongESService;
import com.epam.audio_streaming.service.models.PlayListService;
import com.epam.audio_streaming.service.models.SongService;
import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.models.UserService;
import com.epam.audio_streaming.service.storage.StorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private PlayListService playListService;
    @Autowired
    private UserService userService;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private SongESService songESService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private StorageFactory storageFactory;

    @Value("${path.local.storage}")
    private String pathLocalStorage;

    @Override
    @Transactional
    public Song save(Song song) throws IOException {

        Song song1 = songRepository.save(song);
        songESService.convertorSaveSongToSearch(song1);
        return song1;
    }

    @Override
    public Song saveOnlyDB(Song song) throws IOException {
        return songRepository.save(song);
    }

    @Override
    @Transactional
    public Boolean addSongPlayList(Long id, Long idList, Authentication authentication) {
        Song song = getOne(id);
        User user = userService.findByEmail(authentication.getName());
        if (user.getPlayLists() != null) {
            PlayList list = user.getPlayLists().stream()
                    .filter(a -> Objects.equals(a.getId(), idList))
                    .findFirst()
                    .get();
            if (isNotNull(song, user, list)) {
                list.getSongs().add(song);
                playListService.save(list);
                return true;

            }
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean deleteSongPlayList(Long idSong, Long idList, Authentication authentication) throws IOException {
        Song song = getOne(idSong);
        User user = userService.findByEmail(authentication.getName());
        if (user.getPlayLists() != null) {
            PlayList list = user.getPlayLists().stream()
                    .filter(pl -> Objects.equals(pl.getId(), idList))
                    .findFirst()
                    .get();
            List<Song> songs = new ArrayList<>();
            songs.add(song);
            list.getSongs().removeAll(songs);
            save(song);
            playListService.save(list);

            return true;

        }
        return false;
    }

    @Override
    public boolean existsById(Long idSong) {
        return songRepository.existsById(idSong);
    }

    @Override
    public String saveSong(MultipartFile multipartFile) throws ValidationException {
        try {
            sourceService.sendSource(storageFactory.save(multipartFile.getResource(), multipartFile.getOriginalFilename(),
                    multipartFile.getContentType()));
        } catch (Exception e) {
            throw new ValidationException("Can't save song, something wrong ");
        }
        return "OK";
    }

    @Override
    @Transactional
    public void delete(Song song) {
        songRepository.delete(song);
        deleteSongSearch(song);
    }

    @Override
    public ResponseEntity<byte[]> getSongById(Long id) throws Exception {
        Source source = sourceService.getOne(songESService.findById(id).get().getSourceId());
        byte[] content = IOUtils.toByteArray(storageFactory
                .get(sourceService.getOne(songESService.findById(id).get().getSourceId())));
        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + source.getName() + "\"")
                .body(content);
    }

    @Override
    public Song findByName(String name) {
        return songRepository.findByName(name);
    }

    @Override
    public Song getOne(Long id) {
        return songRepository.getOne(id);
    }

    @Override
    public List<Long> deleteSongs(List<String> ids) throws Exception {
        List<Long> songIds = new ArrayList<>();
        for (String id : ids) {
            Song song = songRepository.getOne(Long.valueOf(id));
            Source source = sourceService.getOne(songESService.findById(Long.valueOf(id)).get().getSourceId());
            if (song == null || source == null) {
                songIds.add(0L);
            } else {
                song.getPlayLists().forEach(pl -> {
                    pl.getSongs().remove(song);
                    playListService.save(pl);
                });
                delete(song);
                storageFactory.delete(source);
                songIds.add(Long.valueOf(id));
            }
        }
        return songIds;
    }

    private boolean isNotNull(Song song, User user, PlayList list) {
        return song != null && user != null && list != null;
    }

    private void deleteSongSearch(Song song) {
        songESService.deleteById(song.getId());
    }

}