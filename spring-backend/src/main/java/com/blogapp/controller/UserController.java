package com.blogapp.controller;

import com.blogapp.dto.request.UserUpdateRequest;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.dto.response.UserResponse;
import com.blogapp.entity.User;
import com.blogapp.security.service.CustomUserDetails;
import com.blogapp.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.blogapp.dto.response.UserCountResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        UserResponse response = userService.getCurrentUser(currentUser);
        return ResponseEntity.ok(ApiResponse.success("User profile fetched successfully", response));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @Valid @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        UserResponse response = userService.updateCurrentUser(request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("User profile updated successfully", response));
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody com.blogapp.dto.request.ChangePasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        userService.changePassword(request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponse> response = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponse> response = userService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Users searched successfully", response));
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserCountResponse>> getUserCount() {
        long count = userService.countAllUsers();
        return ResponseEntity.ok(ApiResponse.success("User count retrieved successfully", new UserCountResponse(count)));
    }
}
