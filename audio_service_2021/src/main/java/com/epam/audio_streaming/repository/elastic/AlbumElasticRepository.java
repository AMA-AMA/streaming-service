package com.epam.audio_streaming.repository.elastic;

import com.epam.audio_streaming.model.elasticsearch.AlbumSearch;

import java.util.List;


public interface AlbumElasticRepository extends GenericElasticRepository<AlbumSearch, Long> {

    List<AlbumSearch> findByNameContainingAndYearAndArtistsNameContainingAndGenresNameContaining(
            String nameAlbum, Integer year, String artist, String genre);

    List<AlbumSearch> findByNameContainingOrYearAndArtistsNameContaining(String nameAlbum, Integer year, String artists);

    List<AlbumSearch> findByNameContainingOrYearAndGenresNameContaining(String nameAlbum, Integer year, String genres);

    List<AlbumSearch> findByNameContainingOrYear(String nameAlbum, Integer year);
}