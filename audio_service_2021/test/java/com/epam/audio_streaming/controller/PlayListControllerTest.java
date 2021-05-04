package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.model.PlayList;
import com.epam.audio_streaming.service.models.PlayListService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ADMIN"}, username = "email@email.com")
@Sql(scripts = "/insert_play_list_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PlayListControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PlayListService playListService;

    private static final Long ID_PLAY_LIST = 7L;
    private static final String NEW_PLAY_LIST = "new_play_list";

    @Test
    void savePlayList() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/play-list/save-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(NEW_PLAY_LIST)))
                .andExpect(status().isOk())
                .andReturn();

        Boolean aBoolean = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Boolean.class);
        assertThat(aBoolean.equals(true));

    }

    @Test
    void getAllPlayLists() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get("/play-list/get-all-list"))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getPlayList() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get("/play-list/get-list/{id}", ID_PLAY_LIST))
                .andExpect(status().isOk())
                .andReturn();

        PlayList playList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PlayList.class);
        assertThat(playList.getId()).isEqualTo(ID_PLAY_LIST);
    }

    @Test
    void deletePlayList() throws Exception {

        this.mockMvc.perform(delete("/play-list/delete-list/{id}", ID_PLAY_LIST))
                .andExpect(status().isOk());
        assertThat(playListService.existsById(ID_PLAY_LIST)).isFalse();

    }
}