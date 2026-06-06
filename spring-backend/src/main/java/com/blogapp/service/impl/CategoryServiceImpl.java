package com.blogapp.service.impl;

import com.blogapp.dto.request.CategoryRequest;
import com.blogapp.dto.response.CategoryResponse;
import com.blogapp.entity.Category;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.exception.UserAlreadyExistsException;
import com.blogapp.mapper.CategoryMapper;
import com.blogapp.repository.BlogRepository;
import com.blogapp.repository.CategoryRepository;
import com.blogapp.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BlogRepository blogRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, BlogRepository blogRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.blogRepository = blogRepository;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new UserAlreadyExistsException("Category already exists with name: " + request.getName());
        }
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getName().equalsIgnoreCase(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new UserAlreadyExistsException("Category already exists with name: " + request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        blogRepository.unsetCategoryForBlogs(id);
        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
