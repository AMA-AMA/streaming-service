package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.ArtistDTO;
import com.epam.audio_streaming.model.elasticsearch.ArtistSearch;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.model.elasticsearch.request.ArtistSearchRequest;
import com.epam.audio_streaming.service.elastic.ArtistESService;
import com.epam.audio_streaming.service.models.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ADMIN"})
@Sql(scripts = "/insert_artist_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private ArtistESService artistESService;

    private static final Long ID_ARTIST = 2L;
    private static final String NAME_ARTIST = "artist";
    private static final String NEW_NAME_ARTIST = "new_artist";

    @BeforeEach
    public void setUp() throws UnknownHostException {
        artistESService.deleteById(2L);
        GenreSearch genreSearch = new GenreSearch(1L, "genre");
        ArtistSearch artistSearch = new ArtistSearch(2L, "artist");
        artistSearch.setGenres(Collections.singletonList(genreSearch));
        artistESService.save(artistSearch);
    }

    @AfterEach
    public void tearDown() throws UnknownHostException {
        artistESService.deleteById(2L);
    }

    @Test
    void createArtist() throws Exception {

        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setName(NEW_NAME_ARTIST);


        MvcResult mvcResult = this.mockMvc.perform(post("/artist/create-artist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        ArtistDTO artist = artistService.getArtistById(id);

        assertThat(artist.getName()).isEqualTo(artistDTO.getName());
    }

    @Test
    void updateArtist() throws Exception {

        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setName(NEW_NAME_ARTIST);

        MvcResult mvcResult = this.mockMvc.perform(put("/artist/update-artist/{id}", ID_ARTIST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ArtistDTO artist = artistService.getArtistById(ID_ARTIST);
        assertEquals(artist.getName(), NEW_NAME_ARTIST);

    }

    @Test
    void getArtist() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get("/artist/get-artist/{id}", ID_ARTIST))
                .andExpect(status().isOk())
                .andReturn();
        ArtistDTO artist = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ArtistDTO.class);
        assertEquals(artist.getName(), NAME_ARTIST);

    }

    @Test
    void deleteArtist() throws Exception {
        this.mockMvc.perform(delete("/artist/delete-artist/{ids}", ID_ARTIST))
                .andExpect(status().isOk());
        assertThat(artistService.existsById(ID_ARTIST)).isFalse();
    }


    @Test
    void searchArtist() throws Exception {

        ArtistSearchRequest artistSearchRequest =
                new ArtistSearchRequest("artist","genre" );

        MvcResult mvcResult = this.mockMvc.perform(get("/artist/search-artist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(artistSearchRequest)))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());

    }
}