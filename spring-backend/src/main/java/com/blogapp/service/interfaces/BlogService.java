package com.blogapp.service.interfaces;

import com.blogapp.dto.request.BlogRequest;
import com.blogapp.dto.response.BlogResponse;
import com.blogapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    BlogResponse createBlog(BlogRequest request, User author);
    BlogResponse updateBlog(Long id, BlogRequest request, User currentUser);
    void deleteBlog(Long id, User currentUser);
    BlogResponse getBlogById(Long id, boolean increment);
    Page<BlogResponse> getAllBlogs(Pageable pageable);
    Page<BlogResponse> searchBlogs(String keyword, Pageable pageable);
    Page<BlogResponse> getBlogsByCategory(Long categoryId, Pageable pageable);
}
