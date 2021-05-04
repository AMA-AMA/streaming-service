package com.epam.audio_streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SongDTO {

    private Long id;

    @Length(min = 5, max = 200)
    private String name;

    @Length(max = 2000)
    private String notes;

    @Min(1900)
    @Max(2021)
    private Integer year;


}
