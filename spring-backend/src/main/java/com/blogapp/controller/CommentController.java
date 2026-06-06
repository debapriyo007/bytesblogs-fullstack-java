package com.blogapp.controller;

import com.blogapp.dto.request.CommentRequest;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.dto.response.CommentResponse;
import com.blogapp.entity.User;
import com.blogapp.security.service.CustomUserDetails;
import com.blogapp.service.interfaces.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/blogs/{blogId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long blogId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        CommentResponse response = commentService.createComment(blogId, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Comment added successfully", response));
    }

    @GetMapping("/blogs/{blogId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsForBlog(@PathVariable Long blogId) {
        List<CommentResponse> response = commentService.getCommentsByBlog(blogId);
        return ResponseEntity.ok(ApiResponse.success("Comments fetched successfully", response));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        CommentResponse response = commentService.updateComment(commentId, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Comment updated successfully", response));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully"));
    }
}
