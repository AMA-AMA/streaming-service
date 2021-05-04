package com.epam.audio_streaming.model.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "song_index")
public class SongSearch {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "notes")
    private String notes;

    @Field(type = FieldType.Integer, name = "year")
    private Integer year;

    @Field(type = FieldType.Long, name = "source")
    private Long sourceId;

    public SongSearch(Long id, String name, String notes, Integer year) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.year = year;
    }
}
