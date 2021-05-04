package com.epam.audio_streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenreDTO {

    private Long id;

    @Length(min = 2, max = 200)
    private String name;

}
