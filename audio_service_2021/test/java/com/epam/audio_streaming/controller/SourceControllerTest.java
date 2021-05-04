package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.service.models.SourceService;
import com.epam.audio_streaming.service.storage.StorageFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = {"ADMIN"})
@Sql(scripts = "/insert_resource_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SourceControllerTest {

    @Autowired
    private StorageFactory storageFactory;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private MockMvc mockMvc;

    private final static String NAME_ZIP = "testapp.zip";

    @Test
    void saveZip() throws Exception {

        File file = new File("test/resources/testapp.zip");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", NAME_ZIP,
                "application/x-zip-compressed", Files.readAllBytes(file.toPath()));

        MvcResult mvcResult = this.mockMvc.perform(multipart("/resource")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("OK");

    }

}