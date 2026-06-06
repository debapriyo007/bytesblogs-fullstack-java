package com.blogapp.service.interfaces;

import com.blogapp.dto.request.LoginRequest;
import com.blogapp.dto.request.RegisterRequest;
import com.blogapp.dto.request.UserUpdateRequest;
import com.blogapp.dto.response.AuthResponse;
import com.blogapp.dto.response.UserResponse;
import com.blogapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser(User currentUser);
    UserResponse updateCurrentUser(UserUpdateRequest request, User currentUser);
    void deleteUser(Long id);
    Page<UserResponse> getAllUsers(Pageable pageable);
    long countAllUsers();
    Page<UserResponse> searchUsers(String keyword, Pageable pageable);
    AuthResponse verifyOtp(com.blogapp.dto.request.VerifyOtpRequest request);
    void resendOtp(String email);
    void changePassword(com.blogapp.dto.request.ChangePasswordRequest request, User currentUser);
}
