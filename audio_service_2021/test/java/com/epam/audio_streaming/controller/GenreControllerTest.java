package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.GenreDTO;
import com.epam.audio_streaming.model.Genre;
import com.epam.audio_streaming.model.elasticsearch.GenreSearch;
import com.epam.audio_streaming.service.elastic.GenreESService;
import com.epam.audio_streaming.service.models.GenreService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ADMIN"})
@Sql(scripts = "/insert_genre_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreESService genreESService;

    private static final Long ID_GENRE = 1L;
    private static final String NAME_GENRE = "genre";
    private static final String NEW_NAME_GENRE = "new_genre";

    @BeforeEach
    public void setUp() throws UnknownHostException {
        genreESService.deleteById(1L);
        GenreSearch genreSearch = new GenreSearch(1L, "genre");
        genreESService.save(genreSearch);
    }

    @AfterEach
    public void tearDown() throws UnknownHostException {
        genreESService.deleteById(1L);
    }

    @Test
    void getGenre() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get("/genres/get-all-genres"))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void createGenre() throws Exception {

        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setName(NEW_NAME_GENRE);

        MvcResult mvcResult = this.mockMvc.perform(post("/genres/create-genre")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreDTO)))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        Genre genre = genreService.getOne(id);
        assertThat(genre.getName()).isEqualTo(genreDTO.getName());

    }

    @Test
    void deleteGenre() throws Exception {

        this.mockMvc.perform(delete("/genres/delete-genre/{ids}", ID_GENRE))
                .andExpect(status().isOk());
        assertThat(genreService.existsById(ID_GENRE)).isFalse();
    }

}

