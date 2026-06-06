package com.blogapp.controller;

import com.blogapp.dto.request.BlogRequest;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.dto.response.BlogResponse;
import com.blogapp.entity.User;
import com.blogapp.security.service.CustomUserDetails;
import com.blogapp.service.interfaces.BlogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BlogResponse>> createBlog(
            @Valid @ModelAttribute BlogRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        BlogResponse response = blogService.createBlog(request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Blog created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogResponse>> updateBlog(
            @PathVariable Long id,
            @Valid @ModelAttribute BlogRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        BlogResponse response = blogService.updateBlog(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Blog updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBlog(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        blogService.deleteBlog(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Blog deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean increment) {
        BlogResponse response = blogService.getBlogById(id, increment);
        return ResponseEntity.ok(ApiResponse.success("Blog fetched successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BlogResponse>>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BlogResponse> response = blogService.getAllBlogs(pageable);
        return ResponseEntity.ok(ApiResponse.success("Blogs fetched successfully", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BlogResponse>>> searchBlogs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BlogResponse> response = blogService.searchBlogs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Blogs searched successfully", response));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<BlogResponse>>> getBlogsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BlogResponse> response = blogService.getBlogsByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Blogs for category fetched successfully", response));
    }
}
