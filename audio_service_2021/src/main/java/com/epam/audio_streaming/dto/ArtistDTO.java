package com.epam.audio_streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArtistDTO {

    private Long id;

    @Length(min = 5, max = 200)
    private String name;

    @Length(max = 2000)
    private String notes;

    private List<Long> genre;


}
