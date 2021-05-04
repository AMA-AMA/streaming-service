package com.epam.audio_streaming.service.elastic.impl;

import com.epam.audio_streaming.model.Song;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.model.elasticsearch.SongSearch;
import com.epam.audio_streaming.repository.elastic.SongElasticRepository;
import com.epam.audio_streaming.service.elastic.SongESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongESServiceImpl implements SongESService {

    @Autowired
    private SongElasticRepository songElasticRepository;

    @Override
    public void convertorSaveSongToSearch(Song song) {

        SongSearch songSearch = new SongSearch(song.getId(), song.getName(), song.getNotes(), song.getYear());
        Source s = song.getSource();
        if (s != null) {
            songSearch.setSourceId(s.getId());
        }
        save(songSearch);
    }

    @Override
    public Optional<SongSearch> findById(Long id) {
        return songElasticRepository.findById(id);
    }

    @Override
    public void save(SongSearch songSearch) {
        songElasticRepository.save(songSearch);
    }

    @Override
    public boolean deleteById(Long id) {
        songElasticRepository.deleteById(id);
        return false;
    }
}
