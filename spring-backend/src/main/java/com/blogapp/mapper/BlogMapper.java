package com.blogapp.mapper;

import com.blogapp.dto.request.BlogRequest;
import com.blogapp.dto.response.BlogResponse;
import com.blogapp.entity.Blog;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class BlogMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

    public BlogMapper(UserMapper userMapper, CategoryMapper categoryMapper, TagMapper tagMapper) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
    }

    public BlogResponse toResponse(Blog blog) {
        if (blog == null) {
            return null;
        }
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .excerpt(blog.getExcerpt())
                .status(blog.getStatus())
                .coverImage(blog.getCoverImage())
                .viewCount(blog.getViewCount())
                .author(userMapper.toResponse(blog.getAuthor()))
                .category(categoryMapper.toResponse(blog.getCategory()))
                .tags(blog.getTags() != null ? blog.getTags().stream().map(tagMapper::toResponse).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }

    public Blog toEntity(BlogRequest request) {
        if (request == null) {
            return null;
        }
        return Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .excerpt(request.getExcerpt())
                .status(request.getStatus())
                .coverImage(request.getCoverImage())
                .build();
    }
}
