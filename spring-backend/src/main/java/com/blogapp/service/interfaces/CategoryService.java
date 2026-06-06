package com.blogapp.service.interfaces;

import com.blogapp.dto.request.CategoryRequest;
import com.blogapp.dto.response.CategoryResponse;
import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getAllCategories();
}
