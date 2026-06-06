package com.blogapp.controller;

import com.blogapp.dto.response.ApiResponse;
import com.blogapp.dto.response.LikeCountResponse;
import com.blogapp.dto.response.LikeStatusResponse;
import com.blogapp.entity.User;
import com.blogapp.security.service.CustomUserDetails;
import com.blogapp.service.interfaces.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs/{blogId}")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like")
    public ResponseEntity<ApiResponse<Void>> likeBlog(
            @PathVariable Long blogId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        likeService.likeBlog(blogId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Blog liked successfully"));
    }

    @DeleteMapping("/like")
    public ResponseEntity<ApiResponse<Void>> unlikeBlog(
            @PathVariable Long blogId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        likeService.unlikeBlog(blogId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Blog unliked successfully"));
    }

    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<LikeCountResponse>> getLikeCount(@PathVariable Long blogId) {
        long count = likeService.getLikeCount(blogId);
        return ResponseEntity.ok(ApiResponse.success("Like count retrieved successfully", new LikeCountResponse(count)));
    }

    @GetMapping("/liked")
    public ResponseEntity<ApiResponse<LikeStatusResponse>> isLiked(
            @PathVariable Long blogId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails != null ? userDetails.getUser() : null;
        boolean liked = likeService.isLikedByCurrentUser(blogId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Like status retrieved successfully", new LikeStatusResponse(liked)));
    }
}
