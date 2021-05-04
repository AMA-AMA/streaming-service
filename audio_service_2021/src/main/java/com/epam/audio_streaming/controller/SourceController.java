package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.model.elasticsearch.AlbumSearch;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.model.elasticsearch.SongSearch;
import com.epam.audio_streaming.repository.elastic.AlbumElasticRepository;
import com.epam.audio_streaming.repository.elastic.ArtistElasticRepository;
import com.epam.audio_streaming.repository.elastic.GenreElasticRepository;
import com.epam.audio_streaming.repository.elastic.SongElasticRepository;
import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/resource")
public class SourceController {

    @Autowired
    private StorageFactory storageFactory;
    @Autowired
    private SourceService sourceService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping
    public String saveZip(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        try {
            sourceService.sendSource(storageFactory.save(multipartFile.getResource(), multipartFile.getOriginalFilename(),
                    multipartFile.getContentType()));
        } catch (Exception e) {
            log.error("Exception save zip " + e.getCause());
        }
        return "OK";
    }

    //delete later
    @Autowired
    private SongElasticRepository songRepository;
    @Autowired
    private GenreElasticRepository genreElasticRepository;
    @Autowired
    private ArtistElasticRepository artistElasticRepository;
    @Autowired
    private AlbumElasticRepository albumElasticRepository;

    @DeleteMapping(value = "/delete-songS/")
    public void cleanElastic() throws Exception {
        for (SongSearch song : songRepository.findAll()) {
            songRepository.delete(song);
        }
        for (ArtistSearch song : artistElasticRepository.findAll()) {
            artistElasticRepository.delete(song);
        }
        for (AlbumSearch song : albumElasticRepository.findAll()) {
            albumElasticRepository.delete(song);
        }
        for (GenreSearch song : genreElasticRepository.findAll()) {
            genreElasticRepository.delete(song);
        }
    }
}