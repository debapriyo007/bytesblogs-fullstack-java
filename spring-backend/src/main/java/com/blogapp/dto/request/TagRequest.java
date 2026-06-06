package com.blogapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagRequest {

    @NotBlank(message = "Tag name cannot be blank")
    @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters")
    private String name;
}
