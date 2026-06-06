package com.blogapp.controller;

import com.blogapp.dto.request.BlogRequest;
import com.blogapp.dto.response.BlogResponse;
import com.blogapp.entity.User;
import com.blogapp.enums.BlogStatus;
import com.blogapp.enums.Role;
import com.blogapp.security.service.CustomUserDetails;
import com.blogapp.service.interfaces.BlogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BlogService blogService;

    @Test
    public void testCreateBlogWithDirectImageUploadSuccess() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test-cover.jpg",
                "image/jpeg",
                "image-bytes".getBytes()
        );

        BlogResponse expectedResponse = BlogResponse.builder()
                .id(1L)
                .title("Multipart Blog")
                .content("Uploading direct images is neat.")
                .excerpt("neat upload")
                .status(BlogStatus.PUBLISHED)
                .coverImage("/uploads/test-unique-cover.jpg")
                .viewCount(0L)
                .build();

        Mockito.when(blogService.createBlog(Mockito.any(BlogRequest.class), Mockito.any(User.class)))
                .thenReturn(expectedResponse);

        User mockUser = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john@gmail.com")
                .role(Role.USER)
                .build();
        CustomUserDetails customUserDetails = new CustomUserDetails(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/blogs")
                        .file(imageFile)
                        .param("title", "Multipart Blog")
                        .param("content", "Uploading direct images is neat.")
                        .param("excerpt", "neat upload")
                        .param("status", "PUBLISHED")
                        .param("categoryName", "Technology")
                        .with(SecurityMockMvcRequestPostProcessors.user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Blog created successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Multipart Blog"))
                .andExpect(jsonPath("$.data.coverImage").value("/uploads/test-unique-cover.jpg"));
    }
}
