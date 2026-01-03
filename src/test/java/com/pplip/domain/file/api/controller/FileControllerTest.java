package com.pplip.domain.file.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplip.domain.file.api.response.FileResponse;
import com.pplip.domain.file.persistence.entity.ImageType;
import com.pplip.domain.file.usecase.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
@DisplayName("FileController 테스트")
@WithMockUser
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FileService fileService;

    @Test
    @DisplayName("성공: 이미지 업로드")
    void upload_Success() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        FileResponse fileResponse = FileResponse.builder().id(1L).path("/path/to/image.jpg").build();
        given(fileService.saveFile(any(), any(ImageType.class), any())).willReturn(fileResponse);

        // When
        ResultActions actions = mockMvc.perform(
                multipart("/file/image")
                        .file(file)
                        .param("imageType", "PROFILE")
                        .with(csrf())
        );

        // Then
        actions.andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.code").value(201)) // Assert that the internal success code is 201 (CREATED)
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.path").value("/path/to/image.jpg"));
    }

    @Test
    @DisplayName("성공: 여러 이미지 업로드")
    void uploads_Success() throws Exception {
        // Given
        MockMultipartFile file1 = new MockMultipartFile("files", "test1.jpg", "image/jpeg", "test data 1".getBytes());
        List<FileResponse> fileResponses = Collections.singletonList(FileResponse.builder().id(1L).path("/path/to/image1.jpg").build());
        given(fileService.saveFiles(any(), any(ImageType.class), any())).willReturn(fileResponses);

        // When
        ResultActions actions = mockMvc.perform(
                multipart("/file/images")
                        .file(file1)
                        .param("imageType", "REVIEW")
                        .with(csrf())
        );

        // Then
        actions.andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.code").value(201)) // Assert that the internal success code is 201 (CREATED)
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].path").value("/path/to/image1.jpg"));
    }

    @Test
    @DisplayName("성공: 이미지 파일 삭제")
    void deleteFile_Success() throws Exception {
        // Given
        Long fileId = 1L;
        FileResponse fileResponse = FileResponse.builder().id(fileId).path("/path/to/image.jpg").build();
        given(fileService.remove(any(Long.class), any(ImageType.class), any())).willReturn(fileResponse);

        // When
        ResultActions actions = mockMvc.perform(
                delete("/file/image/{id}", fileId)
                        .param("imageType", "PROFILE")
                        .with(csrf())
        );

        // Then
        actions.andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.code").value(203)) // Assert that the internal success code is 203 (REMOVED)
                .andExpect(jsonPath("$.data.id").value(fileId));
    }
}
