package com.epam.audio_streaming.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlbumDTO {

    private Long id;

    @Length(min = 5, max = 200)
    private String name;

    @Length(max = 2000)
    private String notes;

    @Min(1900)
    @Max(2021)
    private Integer years;

    private List<Long> genres;

    private List<Long> artists;

    public AlbumDTO(Long id, String name, String notes, Integer year) {
        this.id=id;
        this.name=name;
        this.notes=notes;
        this.years=year;
    }

//    public AlbumDTO() {
//    }
//

}
