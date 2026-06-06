package com.blogapp.service.impl;

import com.blogapp.dto.request.BlogRequest;
import com.blogapp.dto.response.BlogResponse;
import com.blogapp.dto.response.FileResponse;
import com.blogapp.entity.Blog;
import com.blogapp.entity.Category;
import com.blogapp.entity.User;
import com.blogapp.enums.Role;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.exception.UnauthorizedException;
import com.blogapp.mapper.BlogMapper;
import com.blogapp.repository.BlogRepository;
import com.blogapp.repository.CategoryRepository;
import com.blogapp.repository.TagRepository;
import com.blogapp.entity.Tag;
import com.blogapp.service.interfaces.BlogService;
import com.blogapp.service.interfaces.FileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final BlogMapper blogMapper;
    private final FileStorageService fileStorageService;

    public BlogServiceImpl(
            BlogRepository blogRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository,
            BlogMapper blogMapper,
            FileStorageService fileStorageService) {
        this.blogRepository = blogRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.blogMapper = blogMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public BlogResponse createBlog(BlogRequest request, User author) {
        String categoryName = request.getCategoryName().trim();
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(categoryName)
                        .description("Dynamically created via blog creation")
                        .build()));

        Blog blog = blogMapper.toEntity(request);
        
        // Handle direct image file upload if present
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            FileResponse fileResponse = fileStorageService.storeFile(request.getImageFile());
            blog.setCoverImage(fileResponse.getFileUrl());
        }

        blog.setAuthor(author);
        blog.setCategory(category);
        blog.setViewCount(0L);

        // Associate tags
        List<Tag> tags = new ArrayList<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = tagRepository.findAllById(request.getTagIds());
        }
        blog.setTags(tags);

        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.toResponse(savedBlog);
    }

    @Override
    public BlogResponse updateBlog(Long id, BlogRequest request, User currentUser) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        // Check if author or admin
        if (currentUser.getRole() != Role.ADMIN && !blog.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You do not have permission to update this blog");
        }

        String categoryName = request.getCategoryName().trim();
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(categoryName)
                        .description("Dynamically created via blog creation")
                        .build()));

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setExcerpt(request.getExcerpt());
        blog.setStatus(request.getStatus());

        // Handle direct image file upload if present
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            FileResponse fileResponse = fileStorageService.storeFile(request.getImageFile());
            blog.setCoverImage(fileResponse.getFileUrl());
        } else {
            blog.setCoverImage(request.getCoverImage());
        }

        blog.setCategory(category);

        // Associate tags
        List<Tag> tags = new ArrayList<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags = tagRepository.findAllById(request.getTagIds());
        }
        blog.setTags(tags);

        Blog updatedBlog = blogRepository.save(blog);
        return blogMapper.toResponse(updatedBlog);
    }

    @Override
    public void deleteBlog(Long id, User currentUser) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        // Check if author or admin
        if (currentUser.getRole() != Role.ADMIN && !blog.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You do not have permission to delete this blog");
        }

        blogRepository.delete(blog);
    }

    @Override
    public BlogResponse getBlogById(Long id, boolean increment) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        Blog savedBlog = blog;
        if (increment) {
            blog.setViewCount(blog.getViewCount() + 1);
            savedBlog = blogRepository.save(blog);
        }

        return blogMapper.toResponse(savedBlog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogResponse> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable)
                .map(blogMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogResponse> searchBlogs(String keyword, Pageable pageable) {
        org.springframework.data.domain.Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            java.util.List<org.springframework.data.domain.Sort.Order> orders = sort.stream()
                    .map(order -> {
                        String property = order.getProperty();
                        if ("createdAt".equals(property)) {
                            property = "created_at";
                        } else if ("viewCount".equals(property)) {
                            property = "view_count";
                        }
                        return new org.springframework.data.domain.Sort.Order(order.getDirection(), property);
                    })
                    .collect(java.util.stream.Collectors.toList());
            pageable = org.springframework.data.domain.PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    org.springframework.data.domain.Sort.by(orders)
            );
        }
        return blogRepository.searchBlogsNative(keyword, pageable)
                .map(blogMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogResponse> getBlogsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return blogRepository.findByCategoryId(categoryId, pageable)
                .map(blogMapper::toResponse);
    }
}
