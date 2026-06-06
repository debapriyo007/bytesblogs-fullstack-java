package com.blogapp.dto.response;

import com.blogapp.enums.BlogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private String excerpt;
    private BlogStatus status;
    private String coverImage;
    private Long viewCount;
    private UserResponse author;
    private CategoryResponse category;
    private List<TagResponse> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
