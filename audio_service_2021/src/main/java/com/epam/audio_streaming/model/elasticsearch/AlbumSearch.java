package com.epam.audio_streaming.model.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Document(indexName = "album_index")
public class AlbumSearch {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "notes")
    private String notes;

    @Field(type = FieldType.Integer, name = "year")
    private Integer year;

    @Field(type = FieldType.Nested, name = "genres", includeInParent = true)
    private List<GenreSearch> genres;

    @Field(type = FieldType.Nested, name = "artists", includeInParent = true)
    private List<ArtistSearch> artists;

    public AlbumSearch(Long id, String name, String notes, Integer year) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.year = year;
    }

    public AlbumSearch() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlbumSearch)) return false;
        AlbumSearch that = (AlbumSearch) o;
        return this.id == that.id;
    }

}
