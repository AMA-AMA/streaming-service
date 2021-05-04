package com.epam.audio_streaming.service.elastic.impl;

import com.epam.audio_streaming.model.Artist;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.model.elasticsearch.request.ArtistSearchRequest;
import com.epam.audio_streaming.repository.elastic.ArtistElasticRepository;
import com.epam.audio_streaming.service.elastic.ArtistESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class ArtistESServiceImpl implements ArtistESService {

    @Autowired
    private ArtistElasticRepository artistElasticRepository;

    @Override
    public Page<ArtistSearch> searchArtist(ArtistSearchRequest artist) {
        LinkedHashSet<ArtistSearch> hashSet = new LinkedHashSet<>();
        if (artist.getName() != null) {
            hashSet.addAll(artistElasticRepository
                    .findByNameContainingAndGenresNameContaining(artist.getName(), artist.getGenres()));
        } else {
            hashSet.addAll(artistElasticRepository.findByNameContaining(artist.getName()));
        }
        List<ArtistSearch> list = new ArrayList<>(hashSet);
        return new PageImpl<ArtistSearch>(list, PageRequest.of(0, 3), list.size());
    }

    @Override
    public void convertorArtist(Artist artist) {

        List<GenreSearch> g = new ArrayList<>();
        if (artist.getListGenres() != null) {
            artist.getListGenres().forEach(a -> g.add(new GenreSearch(a.getId(), a.getName())));
        }
        artistElasticRepository.save(new ArtistSearch(artist.getId(), artist.getName(), artist.getNotes(), g));
    }

    @Override
    public void deleteById(Long id) {
        artistElasticRepository.deleteById(id);
    }

    @Override
    public void save(ArtistSearch artistSearch) {
        artistElasticRepository.save(artistSearch);
    }

}
