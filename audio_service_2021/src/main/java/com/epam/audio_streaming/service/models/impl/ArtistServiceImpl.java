package com.epam.audio_streaming.service.models.impl;

import com.epam.audio_streaming.dto.ArtistDTO;
import com.epam.audio_streaming.model.Artist;
import com.epam.audio_streaming.model.Genre;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.model.elasticsearch.request.ArtistSearchRequest;
import com.epam.audio_streaming.repository.models.ArtistRepository;
import com.epam.audio_streaming.service.elastic.ArtistESService;
import com.epam.audio_streaming.service.models.ArtistService;
import com.epam.audio_streaming.service.models.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private GenreService genreService;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private ArtistESService artistESService;

    @Override
    @Transactional
    public Artist saveArtist(Artist artist) {
        Artist artist1 = artistRepository.save(artist);
        artistESService.convertorArtist(artist1);
        return artist1;
    }

    @Override
    public Artist saveOnlyDB(Artist artist) {
        return artistRepository.save(artist);
    }

    @Override
    @Transactional
    public void deleteArtist(Artist artist) {
        artistRepository.delete(artist);
        convertorDeleteArtist(artist);
    }

    @Override
    public ArtistDTO getArtistById(Long id) {
        Artist artist = artistRepository.getOne(id);
        return convert(artist);
    }

    @Override
    @Transactional
    public boolean updateArtist(ArtistDTO artistDTO, Long id) {

        if (artistRepository.getOne(id) != null) {
            Artist artist = new Artist(id, artistDTO.getName(), artistDTO.getNotes());
            if (artistDTO.getGenre() != null) {
                artistDTO.getGenre().forEach(a -> artist.getListGenres().add(genreService.getOne(a)));
            }
            artistRepository.save(artist);
            artistESService.deleteById(id);

            List<GenreSearch> g = new ArrayList<>();
            if (artist.getListGenres() != null) {
                artist.getListGenres().forEach(a -> g.add(new GenreSearch(a.getId(), a.getName())));
            }
            artistESService.save(new ArtistSearch(id, artist.getName(), artist.getNotes(), g)); //maybe exception
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Long createArtist(ArtistDTO artistDTO) {

        List<Genre> listGenres = new ArrayList<>();
        if (findByName(artistDTO.getName()) == null) {
            if (artistDTO.getGenre() != null) {
                artistDTO.getGenre().forEach(a -> listGenres.add(genreService.getOne(a)));
            }
            Artist artist = new Artist(artistDTO.getName(), artistDTO.getNotes());
            artist.setListGenres(listGenres);
            return saveArtist(artist).getId();
        }
        return 0L;
    }

    @Override
    public Artist findByName(String name) {
        return artistRepository.findByName(name);
    }

    @Override
    @Transactional
    public List<Long> deleteArtists(List<String> id) {
        List<Long> list = new ArrayList<>();
        for (String a : id) {
            Artist artist = artistRepository.getOne(Long.valueOf(a));
            if (artist == null) {
                list.add(0L);
            } else {
                deleteArtist(artist);
                list.add(Long.valueOf(a));
            }
        }
        return list;
    }

    @Override
    public Artist getOne(Long id) {
        return artistRepository.getOne(id);
    }

    @Override
    public boolean existsById(Long idArtist) {
        return artistRepository.existsById(idArtist);
    }

    @Override
    public Page<ArtistSearch> searchArtist(ArtistSearchRequest artist) {
        return artistESService.searchArtist(artist);
    }

    public void convertorDeleteArtist(Artist artist) {
        artistESService.deleteById(artist.getId());
    }

    private ArtistDTO convert(Artist artist) {

        List<Long> genreDTOS = new ArrayList<>();
        if (artist.getListGenres() != null) {
            artist.getListGenres().forEach(a -> genreDTOS.add(a.getId()));
        }
        return new ArtistDTO(artist.getId(), artist.getName(), artist.getNotes(), genreDTOS);
    }

}

