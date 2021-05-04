package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.model.PlayList;
import com.epam.audio_streaming.model.elasticsearch.SongSearch;
import com.epam.audio_streaming.service.elastic.SongESService;
import com.epam.audio_streaming.service.models.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.net.UnknownHostException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ADMIN"}, username = "email@email.com")
@Sql(scripts = "/insert_song_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SongControllerTest {

    @Autowired
    private SongService songService;
    @Autowired
    private SongESService songESService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final static String NAME_SONG = "Audio1.mp3";
    private final static Long ID_SONG = 5L;
    private final static Long ID_PLAY_LIST = 7L;

    @BeforeEach
    public void setUp() throws UnknownHostException {
        songESService.deleteById(5L);
        SongSearch songSearchSearch = new SongSearch(5L, "Audio2.mp3", "eng - good music", 1999);
        songSearchSearch.setSourceId(1L);
        songESService.save(songSearchSearch);
    }

    @AfterEach
    public void tearDown() throws UnknownHostException {
        songESService.deleteById(5L);
    }

    @Test
    void saveSong() throws Exception {

        File file = new File("test/resources/Audio1.mp3");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", NAME_SONG,
                "audio/mpeg", Files.readAllBytes(file.toPath()));

        MvcResult mvcResult = this.mockMvc.perform(multipart("/song/save-songs")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("OK");

    }

    @Test
    void getById() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/song/get-song/{id}", ID_SONG))
                .andExpect(status().isOk())
                .andReturn();

        Long sizeSong = objectMapper.readValue(mvcResult.getResponse().getHeader("Content-Length"), Long.class);
        Long size = songService.getOne(ID_SONG).getSource().getSize();
        assertThat(sizeSong).isEqualTo(size);

    }

    @Test
    void deleteById() throws Exception {

        this.mockMvc.perform(delete("/song/delete-song/{ids}", ID_SONG))
                .andExpect(status().isOk());

        assertThat(songService.existsById(ID_SONG)).isFalse();

    }

    @Test
    void addSongPlayList() throws Exception {

        this.mockMvc.perform(get("/song/add-song-playlist/{id}/{list}", ID_SONG, ID_PLAY_LIST))
                .andExpect(status().isOk());

        PlayList playList = songService.getOne(ID_SONG).getPlayLists().stream()
                .filter(a -> a.getId().equals(ID_PLAY_LIST))
                .findFirst()
                .get();
        assertThat(playList.getId()).isEqualTo(ID_PLAY_LIST);
    }

    @Test
    void deleteSongFromPlayList() throws Exception {
        songService.addSongPlayList(ID_SONG, ID_PLAY_LIST, SecurityContextHolder.getContext().getAuthentication());
        PlayList playList = songService.getOne(ID_SONG).getPlayLists().stream()
                .filter(a -> a.getId().equals(ID_PLAY_LIST))
                .findFirst()
                .get();

        assertThat(playList.getId().equals(ID_PLAY_LIST));

        this.mockMvc.perform(delete("/song/delete-song-playlist/{id}/{list}", ID_SONG, ID_PLAY_LIST))
                .andExpect(status().isOk());

        assertThat(songService.getOne(ID_SONG).getPlayLists()).isEmpty();


    }


}