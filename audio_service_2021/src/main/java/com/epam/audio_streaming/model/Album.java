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
@Table(name = "albums")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Album implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String notes;

    private Integer year;

    @OneToOne(mappedBy = "album", fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id")
    private Participation participation;

    @OneToMany(mappedBy = "album", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    private List<Song> songs = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "album_artist",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> listArtists = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "album_genre",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> listGenres = new ArrayList<>();

    public Album(String name, Integer year, String notes) {
        this.name = name;
        this.year = year;
        this.notes = notes;
    }

    public Album(Long id, String name, String notes, Integer years) {
        this.id=id;
        this.name = name;
        this.notes = notes;
        this.year=years;
    }

    public Album(String name, String notes, Integer year) {
        this.name = name;
        this.notes = notes;
        this.year = year;
    }

    public void setSong(List<Song> songs) {
        if (songs != null) {
            songs.forEach(b -> {
                b.setAlbum(this);
            });
        }
        this.songs = songs;
    }

}
