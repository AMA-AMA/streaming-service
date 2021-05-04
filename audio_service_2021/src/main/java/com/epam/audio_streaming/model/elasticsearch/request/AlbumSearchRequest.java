package com.epam.audio_streaming.model.elasticsearch.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class AlbumSearchRequest {

    private String name;

    @Min(1900)
    @Max(2021)
    private Integer year;

    private String genres;

    private String artists;

    public AlbumSearchRequest(String name, @Min(1900) @Max(2021) Integer year, String genres, String artists) {
        this.name = name;
        this.year = year;
        this.genres = genres;
        this.artists = artists;
    }
}
