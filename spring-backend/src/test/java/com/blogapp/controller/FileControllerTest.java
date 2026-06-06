package com.blogapp.controller;

import com.blogapp.dto.response.FileResponse;
import com.blogapp.service.interfaces.FileStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileStorageService fileStorageService;

    @Test
    @WithMockUser(username = "test_user")
    public void testUploadFileSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "test-image-content".getBytes()
        );

        FileResponse expectedResponse = FileResponse.builder()
                .fileName("test-unique-uuid.png")
                .fileUrl("/uploads/test-unique-uuid.png")
                .fileType("image/png")
                .fileSize(file.getSize())
                .build();

        Mockito.when(fileStorageService.storeFile(Mockito.any())).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("File uploaded successfully"))
                .andExpect(jsonPath("$.data.fileName").value("test-unique-uuid.png"))
                .andExpect(jsonPath("$.data.fileUrl").value("/uploads/test-unique-uuid.png"))
                .andExpect(jsonPath("$.data.fileType").value("image/png"))
                .andExpect(jsonPath("$.data.fileSize").value(file.getSize()));
    }

    @Test
    public void testUploadFileUnauthenticated() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "test-image-content".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                        .file(file))
                .andExpect(status().isForbidden());
    }
}
