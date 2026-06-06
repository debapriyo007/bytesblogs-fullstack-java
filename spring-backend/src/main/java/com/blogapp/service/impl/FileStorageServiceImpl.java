package com.blogapp.service.impl;

import com.blogapp.dto.response.FileResponse;
import com.blogapp.exception.InvalidFileException;
import com.blogapp.service.interfaces.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final CloudinaryService cloudinaryService;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");

    public FileStorageServiceImpl(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public FileResponse storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Failed to store empty file.");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileException("Only image uploads (JPEG, PNG, GIF, WEBP) are allowed.");
        }

        // Normalize file name and extract extension
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getFileExtension(originalFileName);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new InvalidFileException("Invalid file extension. Only image files are allowed.");
        }

        if (!cloudinaryService.isConfigured()) {
            throw new IllegalStateException("Cloudinary is not configured. Uploads are disabled.");
        }

        try {
            String secureUrl = cloudinaryService.uploadFile(file);
            return FileResponse.builder()
                    .fileName(originalFileName)
                    .fileUrl(secureUrl)
                    .fileType(contentType)
                    .fileSize(file.getSize())
                    .build();
        } catch (IOException ex) {
            throw new RuntimeException("Could not upload file to Cloudinary: " + ex.getMessage(), ex);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
