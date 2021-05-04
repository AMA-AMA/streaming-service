package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.UserRegistrationDTO;
import com.epam.audio_streaming.model.User;
import com.epam.audio_streaming.service.models.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/insert_user_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    private final static String FIRST_NAME = "Koly";
    private final static String LAST_NAME = "Rob";
    private final static String EMAIL = "Koly@email.com";
    private final static String PASSWORD = "123";
    private final static String CODE = "260399da-3c99-4867-a878-e12c73e8bc4e";

    @Test
    void registration() throws Exception {

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);

        MvcResult mvcResult = this.mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        Boolean aBoolean = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Boolean.class);
        assertThat(aBoolean.equals(true));

        User user = userService.findByEmail(EMAIL);
        assertThat(user.getFirstName()).isEqualTo(registrationDTO.getFirstName());


    }

    @Test
    void activate() throws Exception {

        User user = new User(FIRST_NAME, LAST_NAME, EMAIL, CODE);
        userService.save(user);

        MvcResult mvcResult = this.mockMvc.perform(get("/registration/activate/{code}",CODE))
                .andExpect(status().isOk())
                .andReturn();
        Boolean aBoolean = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Boolean.class);
        assertThat(aBoolean.equals(true));

        User userDB = userService.findByEmail(EMAIL);
        assertThat(userDB.getActivationCode()).isEqualTo(null);
    }

}