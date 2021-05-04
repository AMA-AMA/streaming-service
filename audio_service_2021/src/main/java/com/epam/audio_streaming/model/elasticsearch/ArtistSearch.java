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
@Document(indexName = "artists_index")
public class ArtistSearch {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "notes")
    private String notes;

    @Field(type = FieldType.Nested, name = "genres", includeInParent = true)
    private List<GenreSearch> genres;

    public ArtistSearch(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ArtistSearch(Long id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    public ArtistSearch() {
    }

    public ArtistSearch(Long id, String name, String notes, List<GenreSearch> genres) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.genres = genres;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtistSearch)) return false;
        ArtistSearch that = (ArtistSearch) o;
        return this.id == that.id;
    }
}



