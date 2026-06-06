package com.blogapp.controller;

import com.blogapp.dto.response.ApiResponse;
import com.blogapp.dto.response.FileResponse;
import com.blogapp.service.interfaces.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileResponse>> uploadFile(@RequestParam("file") MultipartFile file) {
        FileResponse response = fileStorageService.storeFile(file);
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", response));
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<ApiResponse<List<FileResponse>>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        List<FileResponse> responses = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    responses.add(fileStorageService.storeFile(file));
                }
            }
        }
        return ResponseEntity.ok(ApiResponse.success("Files uploaded successfully", responses));
    }
}
