package com.epam.audio_streaming.service.impl;

import com.epam.audio_streaming.exception.NotFoundException;
import com.epam.audio_streaming.model.*;
import com.epam.audio_streaming.service.SongInfoService;
import com.epam.audio_streaming.service.models.*;
import com.epam.audio_streaming.service.storage.StorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Slf4j
@Service
public class SongInfoServiceImpl implements SongInfoService {

    @Autowired
    private AlbumService albumService;
    @Autowired
    private SongService songService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private StorageFactory storageFactory;

    @Override
    public Long saveAudio(InputStream input, Source source) throws Exception {

            BufferedInputStream inputStream = new BufferedInputStream(input);
            Metadata metadata = new Metadata();
            new Mp3Parser().parse(inputStream, new DefaultHandler(), metadata, new ParseContext());
            input.close();

            Integer year = getInteger(metadata);

            AudioInfo audioInfo = getAudioInfo(metadata);

            if (audioInfo != null) {

                Genre genre1 = getGenre(audioInfo);

                Artist artist1 = getArtist(audioInfo);

                Album album1 = getAlbum(year, audioInfo);

                Song song1 = getSong(source, year, audioInfo);

                extracted(source, genre1, artist1, album1, song1);

                saveEntities(genre1, artist1, album1, song1);

                return song1.getId();

            } else {
                storageFactory.delete(source);
                throw new NotFoundException("File is empty");
            }
        }

    private void saveEntities(Genre genre1, Artist artist1, Album album1, Song song1) throws IOException {
        genreService.save(genre1);
        songService.save(song1);
        albumService.save(album1);
        artistService.saveArtist(artist1);
    }

    private void extracted(Source source, Genre genre1, Artist artist1, Album album1, Song song1) {
        artist1.setListGenres(Collections.singletonList(genre1));
        album1.setListGenres(Collections.singletonList(genre1));
        album1.setListArtists(Collections.singletonList(artist1));
        song1.setAlbum(album1);
        song1.setArtist(artist1);
        song1.setGenre(genre1);
        song1.setSource(source);
    }

    private Song getSong(Source source, Integer year, AudioInfo audioInfo) throws IOException {
        Song song1 = songService.findByName(source.getName());
        if (song1 == null) {
            song1 = songService.saveOnlyDB(new Song(source.getName(), audioInfo.getNotes(), year));
        }
        return song1;
    }

    private Album getAlbum(Integer year, AudioInfo audioInfo) {
        Album album1 = albumService.findByName(audioInfo.getAlbum());
        if (album1 == null) {
            album1 = albumService.saveOnlyDB(new Album(audioInfo.getAlbum(), year, audioInfo.getNotes()));
        }
        return album1;
    }

    private Artist getArtist(AudioInfo audioInfo) {
        Artist artist1 = artistService.findByName(audioInfo.getArtist());
        if (artist1 == null) {
            artist1 = artistService.saveOnlyDB(new Artist(audioInfo.getArtist(), audioInfo.getNotes()));
        }
        return artist1;
    }

    private Genre getGenre(AudioInfo audioInfo) {
        Genre genre1 = genreService.findByName(audioInfo.getGenre());
        if (genre1 == null) {
            genre1 = genreService.saveOnlyDB(new Genre(audioInfo.getGenre()));
        }
        return genre1;
    }

    private AudioInfo getAudioInfo(Metadata metadata) {
        AudioInfo audioInfo = AudioInfo.builder()
                .album(metadata.get("xmpDM:album"))
                .artist(metadata.get("xmpDM:artist"))
                .genre(metadata.get("xmpDM:genre"))
                .title(metadata.get("title"))
                .notes(metadata.get("xmpDM:logComment"))
                .build();
        return audioInfo;
    }

    private Integer getInteger(Metadata metadata) {
        Integer year = 0;
        if (metadata.get("xmpDM:releaseDate") != null && !metadata.get("xmpDM:releaseDate").isEmpty()) {
            year = Integer.parseInt(metadata.get("xmpDM:releaseDate").substring(0, 4));
        }
        return year;
    }

}