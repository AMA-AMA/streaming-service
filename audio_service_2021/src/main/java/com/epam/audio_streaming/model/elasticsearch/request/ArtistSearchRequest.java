package com.epam.audio_streaming.model.elasticsearch.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistSearchRequest {

    private String name;

    private String genres;

    public ArtistSearchRequest(String name, String genres) {
        this.name = name;
        this.genres = genres;

    }

    public ArtistSearchRequest(String name) {
        this.name = name;
    }
}
