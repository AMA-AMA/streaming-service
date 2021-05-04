package com.epam.audio_streaming.service.models.impl;

import com.epam.audio_streaming.dto.AlbumDTO;
import com.epam.audio_streaming.model.Album;
import com.epam.audio_streaming.model.Artist;
import com.epam.audio_streaming.model.Genre;
import com.epam.audio_streaming.model.elasticsearch.AlbumSearch;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.model.elasticsearch.request.AlbumSearchRequest;
import com.epam.audio_streaming.repository.models.AlbumRepository;
import com.epam.audio_streaming.service.elastic.AlbumESService;
import com.epam.audio_streaming.service.models.AlbumService;
import com.epam.audio_streaming.service.models.ArtistService;
import com.epam.audio_streaming.service.models.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumESService albumESService;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private GenreService genreService;

    @Override
    @Transactional
    public Long create(AlbumDTO albumDTO) {

        if (findByName(albumDTO.getName()) == null) {
            List<Genre> listGenres = new ArrayList<>();
            List<Artist> listArtists = new ArrayList<>();
            if (albumDTO.getGenres() != null) {
                albumDTO.getGenres().forEach(a -> listGenres.add(genreService.getOne(a)));
            }
            if (albumDTO.getArtists() != null) {
                albumDTO.getArtists().forEach(a -> listArtists.add(artistService.getOne(a)));
            }
            Album album = new Album(albumDTO.getName(), albumDTO.getNotes(), albumDTO.getYears());
            album.setListArtists(listArtists);
            album.setListGenres(listGenres);
            return save(album).getId();
        }
        return 0L;
    }

    @Override
    @Transactional
    public Album save(Album album) {
        Album album1 = albumRepository.save(album);
        albumESService.convertorAndSaveAlbum(album1);
        return album1;
    }

    @Override
    public Album saveOnlyDB(Album album) {
        return albumRepository.save(album);
    }

    @Override
    public boolean existsById(Long idAlbum) {
        return albumRepository.existsById(idAlbum);
    }

    @Override
    public Page<AlbumSearch> searchAlbum(AlbumSearchRequest album) {
        return albumESService.searchAlbum(album);
    }

    @Override
    @Transactional
    public void delete(Album album) {
        albumRepository.delete(album);
        albumESService.deleteById(album.getId());
    }

    @Override
    @Transactional
    public List<Long> deleteAlbum(List<String> ids) {

        List<Long> list = new ArrayList<>();
        for (String a : ids) {
            if (!a.equals(null)) {
                Album album = albumRepository.getOne(Long.valueOf(a));
                if (album == null) {
                    list.add(0L);
                } else {
                    delete(album);
                    list.add(Long.valueOf(a));
                }
            }
        }
        return list;
    }

    @Override
    public Album findByName(String name) {
        return albumRepository.findByName(name);
    }

    @Override
    public AlbumDTO getAlbum(Long id) {
        Album album = albumRepository.getOne(id);
        return convert(album);
    }

    @Override
    public void updateAlbum(AlbumDTO albumDTO, Long id) {
        Album album = albumRepository.getOne(id);
        if (album != null) {
            albumESService.save(fillUpEntities(albumDTO, id));
        } else {
            throw new EntityNotFoundException("Not found album");
        }
    }

    private AlbumDTO convert(Album album) {

        AlbumDTO u = new AlbumDTO(album.getId(), album.getName(), album.getNotes(), album.getYear());
        List<Long> genreDTO = new ArrayList<>();
        if (album.getListGenres() != null) {
            album.getListGenres().forEach(a -> {
                genreDTO.add(a.getId());
            });
        }
        List<Long> artistDTO = new ArrayList<>();
        if (album.getListArtists() != null) {
            album.getListArtists().forEach(a -> {
                artistDTO.add(a.getId());
            });
        }
        u.setGenres(genreDTO);
        u.setArtists(artistDTO);
        return u;
    }

    private AlbumSearch fillUpEntities(AlbumDTO albumDTO, Long id) {
        Album album = new Album(id, albumDTO.getName(), albumDTO.getNotes(), albumDTO.getYears());
        if (albumDTO.getGenres() != null) {
            albumDTO.getGenres().forEach(a -> album.getListGenres().add(genreService.getOne(a)));
        }
        if (albumDTO.getArtists() != null)
            albumDTO.getArtists().forEach(a -> album.getListArtists().add(artistService.getOne(a)));
        albumRepository.save(album);
        albumESService.deleteById(id);

        List<GenreSearch> genreSearches = new ArrayList<>();
        if (album.getListGenres() != null) {
            album.getListGenres().forEach(a ->
                    genreSearches.add(new GenreSearch(a.getId(), a.getName())));
        }
        List<ArtistSearch> artistSearches = new ArrayList<>();
        if (album.getListArtists() != null) {
            album.getListArtists().forEach(a -> artistSearches.add(new ArtistSearch(a.getId(), a.getName(), a.getNotes())));
        }
        AlbumSearch albumSearch = new AlbumSearch(id, album.getName(), album.getNotes(), album.getYear());
        albumSearch.setGenres(genreSearches);
        albumSearch.setArtists(artistSearches);
        return albumSearch;
    }

}
