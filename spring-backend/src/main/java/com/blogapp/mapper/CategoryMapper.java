package com.blogapp.mapper;

import com.blogapp.dto.request.CategoryRequest;
import com.blogapp.dto.response.CategoryResponse;
import com.blogapp.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}
