package com.epam.audio_streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayListDTO {

    Long id;

    @Length(min = 3, max = 30)
    private String name;

    private List<SongDTO> songs = new ArrayList<>();

    public PlayListDTO(Long id, @Length(min = 3, max = 30) String name) {
        this.id = id;
        this.name = name;
    }
}
