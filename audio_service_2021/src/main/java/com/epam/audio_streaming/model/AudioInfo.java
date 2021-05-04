package com.epam.audio_streaming.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AudioInfo {

    String title;

    String artist;

    String genre;

    String album;

    String year;

    String notes;

}
