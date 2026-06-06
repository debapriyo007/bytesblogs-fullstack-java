package com.blogapp.dto.request;

import com.blogapp.enums.BlogStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @Size(max = 500, message = "Excerpt must be less than 500 characters")
    private String excerpt;

    @NotNull(message = "Blog status is required")
    private BlogStatus status;

    private String coverImage;

    private MultipartFile imageFile;

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    private String categoryName;

    private List<Long> tagIds;
}
