package com.epam.audio_streaming.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "resource")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Source implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long size;

    private String path;

    private String checksum;

    private String name;

    private String fileTypes;

    @Enumerated(EnumType.STRING)
    private StorageTypes storageTypes;

    @OneToOne(mappedBy = "source", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Song song;

    public Source(String name,Long size, String checksum,String path, String fileTypes) {
        this.name = name;
        this.size = size;
        this.checksum = checksum;
        this.path = path;
        this.fileTypes = fileTypes;
    }

    //    public void setSong(Song song) {
//        if (song != null) {
//            song.setSource(this);
//        }
//        this.song = song;
//    }
}
