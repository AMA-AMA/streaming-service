package com.epam.audio_streaming.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "genres")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "listGenres", fetch = FetchType.LAZY)
    private List<Artist> artists;

    @ManyToMany(mappedBy = "listGenres", fetch = FetchType.LAZY)
    private List<Album> albums;

    @OneToMany(mappedBy = "genre", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    private List<Song> songs = new ArrayList<>();

    public Genre(String name) {
        this.name=name;
    }

    public void setSongs(List<Song> songs) {
        if (songs != null) {
            songs.forEach(b -> {
                b.setGenre(this);
            });
        }
        this.songs = songs;
    }


}
