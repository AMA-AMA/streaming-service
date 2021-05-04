package com.epam.audio_streaming.service.elastic.impl;

import com.epam.audio_streaming.model.Album;
import com.epam.audio_streaming.model.elasticsearch.AlbumSearch;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.model.elasticsearch.request.AlbumSearchRequest;
import com.epam.audio_streaming.repository.elastic.AlbumElasticRepository;
import com.epam.audio_streaming.service.elastic.AlbumESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class AlbumESServiceImpl implements AlbumESService {

    @Autowired
    private AlbumElasticRepository albumElasticRepository;

    @Override
    public void deleteById(Long id) {
        albumElasticRepository.deleteById(id);
    }

    @Override
    public void save(AlbumSearch albumSearch) {
        albumElasticRepository.save(albumSearch);
    }

    @Override
    public void convertorAndSaveAlbum(Album album) {

        List<GenreSearch> genres = new ArrayList<>();
        List<ArtistSearch> artists = new ArrayList<>();

        extractedArtistsAndGenres(album, genres, artists);

        AlbumSearch albumSearch = new AlbumSearch(album.getId(), album.getName(), album.getNotes(), album.getYear());
        albumSearch.setGenres(genres);
        albumSearch.setArtists(artists);
        save(albumSearch);
    }

    @Override
    public Page<AlbumSearch> searchAlbum(AlbumSearchRequest album) {
        LinkedHashSet<AlbumSearch> hashSet = new LinkedHashSet<>();
        checkAttributes(album, hashSet);
        List<AlbumSearch> list = new ArrayList<>(hashSet);
        return new PageImpl<AlbumSearch>(list, PageRequest.of(0, 3), list.size());
    }

    private void extractedArtistsAndGenres(Album album1, List<GenreSearch> genres, List<ArtistSearch> artists) {
        if (album1.getListGenres() != null) {
            fillUpGenreSearch(album1, genres);
        }
        if (album1.getListArtists() != null) {
            album1.getListArtists().forEach(a -> {
                artists.add(new ArtistSearch(a.getId(), a.getName(), a.getNotes()));
            });
        }
    }

    private void fillUpGenreSearch(Album album1, List<GenreSearch> genres) {
        album1.getListGenres().forEach(a -> {
            GenreSearch genreSearch = new GenreSearch();
            genreSearch.setId(a.getId());
            genreSearch.setName(a.getName());
            genres.add(genreSearch);
        });
    }

    private void checkAttributes(AlbumSearchRequest album,
                                 LinkedHashSet<AlbumSearch> hashSet) {

        if (album.getArtists() != null && album.getGenres() != null) {
            hashSet.addAll(albumElasticRepository
                    .findByNameContainingAndYearAndArtistsNameContainingAndGenresNameContaining(
                            album.getName(),  album.getYear(), album.getArtists(), album.getGenres()));

        }
        if (album.getGenres() == null && album.getArtists() != null) {
            hashSet.addAll(albumElasticRepository
                    .findByNameContainingOrYearAndArtistsNameContaining(album.getName(),album.getYear(), album.getArtists()));
        }
        if (album.getArtists() == null && album.getGenres() != null) {

            hashSet.addAll(albumElasticRepository
                    .findByNameContainingOrYearAndGenresNameContaining(
                            album.getName(),album.getYear(), album.getGenres()));

        } else {
            hashSet.addAll(albumElasticRepository
                    .findByNameContainingOrYear(album.getName(),album.getYear()));
        }
    }

}
