package com.epam.audio_streaming.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "artists")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Artist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String notes;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "artist_genre",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> listGenres = new ArrayList<>();

    @ManyToMany(mappedBy = "listArtists", fetch = FetchType.LAZY)
    private List<Album> albums;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Participation> participation = new ArrayList<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Song> songs = new ArrayList<>();

    public Artist(Long id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    public Artist(String name, String notes) {
        this.name = name;
        this.notes = notes;

    }

    public void setParticipation(List<Participation> participation) {
        if (participation != null) {
            participation.forEach(a -> {
                a.setArtist(this);
            });
        }
        this.participation = participation;
    }

    public void setSongs(List<Song> songs) {
        if (songs != null) {
            songs.forEach(a -> {
                a.setArtist(this);
            });
        }
        this.songs = songs;
    }

}
