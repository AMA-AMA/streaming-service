package com.epam.audio_streaming.service.models.impl;

import com.epam.audio_streaming.dto.GenreDTO;
import com.epam.audio_streaming.exception.NotFoundException;
import com.epam.audio_streaming.model.Genre;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.repository.models.GenreRepository;
import com.epam.audio_streaming.service.elastic.GenreESService;
import com.epam.audio_streaming.service.models.AlbumService;
import com.epam.audio_streaming.service.models.ArtistService;
import com.epam.audio_streaming.service.models.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private ArtistService artistService;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreESService genreESService;
    @Autowired
    private AlbumService albumService;

    @Override
    public void create(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public Page<Genre> readAll() {
        Pageable pageable = PageRequest.of(0, 10);
        return genreRepository.findAll(pageable);
    }

    @Override
    public Page<GenreDTO> readAllGenre() throws NotFoundException {
        Page<Genre> genre = readAll();
        if (genre != null) {
            List<GenreDTO> genreDTOList = new ArrayList<>();
            genre.forEach(g -> genreDTOList.add(convert(g)));
            return new PageImpl<>(genreDTOList, PageRequest.of(0, 3), genreDTOList.size());
        } else {
            throw new NotFoundException();
        }

    }

    @Override
    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }


    @Override
    @Transactional
    public Long createGenre(GenreDTO genreDTO) {
        if (findByName(genreDTO.getName()) == null) {
            return save(new Genre(genreDTO.getName())).getId();
        }
        return 0L;
    }

    @Override
    public Genre save(Genre genre) {
        Genre genre1 = genreRepository.save(genre);
        genreESService.save(new GenreSearch(genre1.getId(), genre1.getName()));
        return genre1;
    }

    @Override
    public Genre saveOnlyDB(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        genreRepository.delete(genre);
        genreESService.deleteById(genre.getId());
    }

    @Override
    @Transactional
    public List<Long> deleteGenres(List<String> ids) {
        List<Long> list = new ArrayList<>();
        for (String g : ids) {
            Genre genre = genreRepository.getOne(Long.valueOf(g));
            if (genre == null) {
                list.add(0L);
            }
            if (genre.getArtists() != null) {
                genre.getArtists().forEach(a -> {
                    a.getListGenres().remove(genre);
                    artistService.saveArtist(a);
                });
            }
            if (genre.getAlbums() != null) {
                genre.getAlbums().forEach(a -> {
                    a.getListGenres().remove(genre);
                    albumService.save(a);
                });
            }
            delete(genre);
            list.add(Long.valueOf(g));
        }
        return list;
    }

    @Override
    public Genre getOne(Long id) {
        return genreRepository.getOne(id);
    }

    @Override
    public boolean existsById(Long idGenre) {
        return genreRepository.existsById(idGenre);
    }

    private GenreDTO convert(Genre source) {

        GenreDTO u = new GenreDTO();
        u.setId(source.getId());
        u.setName(source.getName());

        return u;
    }

}

