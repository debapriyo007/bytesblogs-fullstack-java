package com.blogapp.controller;

import com.blogapp.dto.request.LoginRequest;
import com.blogapp.dto.request.RegisterRequest;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.dto.response.AuthResponse;
import com.blogapp.dto.response.UserResponse;
import com.blogapp.security.jwt.JwtService;
import com.blogapp.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        AuthResponse authResponse = userService.login(request);

        ResponseCookie cookie = ResponseCookie.from("jwt_token", authResponse.getToken())
                .httpOnly(true)
                .secure(false) // Set to true in production over HTTPS
                .path("/")
                .maxAge(jwtService.getJwtExpirationInSeconds())
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse.getUser()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt_token", "")
                .httpOnly(true)
                .secure(false) // Set to true in production over HTTPS
                .path("/")
                .maxAge(0) // Expire immediately
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(ApiResponse.success("Logout successful"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<UserResponse>> verifyOtp(
            @Valid @RequestBody com.blogapp.dto.request.VerifyOtpRequest request,
            HttpServletResponse response) {
        AuthResponse authResponse = userService.verifyOtp(request);

        ResponseCookie cookie = ResponseCookie.from("jwt_token", authResponse.getToken())
                .httpOnly(true)
                .secure(false) // Set to true in production over HTTPS
                .path("/")
                .maxAge(jwtService.getJwtExpirationInSeconds())
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiResponse.success("Verification and login successful", authResponse.getUser()));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@RequestParam String email) {
        userService.resendOtp(email);
        return ResponseEntity.ok(ApiResponse.success("A new verification OTP code has been dispatched."));
    }
}
