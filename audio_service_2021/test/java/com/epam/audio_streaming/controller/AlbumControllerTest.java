package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.AlbumDTO;
import com.epam.audio_streaming.model.elasticsearch.AlbumSearch;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.model.elasticsearch.request.AlbumSearchRequest;
import com.epam.audio_streaming.service.elastic.AlbumESService;
import com.epam.audio_streaming.service.models.AlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.UnknownHostException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ADMIN"})
@Sql(scripts = "/insert_album_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumESService albumESService;

    private static final Long ID_ALBUM = 3L;
    private static final String NAME_ALBUM = "album";
    private static final String NEW_NAME_ALBUM = "new_album";

    @BeforeEach
    public void setUp() throws UnknownHostException {
        albumESService.deleteById(3L);
        GenreSearch genreSearch = new GenreSearch(1L, "genre");
        ArtistSearch artistSearch = new ArtistSearch(2L, "artist");
        AlbumSearch albumSearch = new AlbumSearch(3L, "album", "notes", 2000);
        albumSearch.setGenres(Collections.singletonList(genreSearch));
        albumSearch.setArtists(Collections.singletonList(artistSearch));
        albumESService.save(albumSearch);
    }

    @AfterEach
    public void tearDown() throws UnknownHostException {
        albumESService.deleteById(3L);
    }

    @Test
    void createAlbum() throws Exception {
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setName(NEW_NAME_ALBUM);

        MvcResult mvcResult = this.mockMvc.perform(post("/album/create-album")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        AlbumDTO album = albumService.getAlbum(id);
        assertThat(album.getName()).isEqualTo(albumDTO.getName());
    }

    @Test
    void updateAlbum() throws Exception {

        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setName(NEW_NAME_ALBUM);

        MvcResult mvcResult = this.mockMvc.perform(put("/album/update-album/{id}", ID_ALBUM)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isOk())
                .andReturn();

        AlbumDTO albumDTO1 = albumService.getAlbum(ID_ALBUM);
        assertEquals(albumDTO1.getName(), NEW_NAME_ALBUM);
    }

    @Test
    void getAlbum() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/album/get-album/{id}", ID_ALBUM))
                .andExpect(status().isOk())
                .andReturn();
        AlbumDTO albumDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AlbumDTO.class);
        assertEquals(albumDTO.getName(), NAME_ALBUM);
    }

    @Test
    void deleteAlbum() throws Exception {

        this.mockMvc.perform(delete("/album/delete-album/{ids}", ID_ALBUM))
                .andExpect(status().isOk());
        assertThat(albumService.existsById(ID_ALBUM)).isFalse();
    }

    @Test
    void searchAlbum() throws Exception {

        AlbumSearchRequest albumSearchRequest =
                new AlbumSearchRequest("album",2000,"genre","artist");

        MvcResult mvcResult = this.mockMvc.perform(get("/album/search-album")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(albumSearchRequest)))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertNotNull(mvcResult.getResponse().getContentAsString());
    }
}