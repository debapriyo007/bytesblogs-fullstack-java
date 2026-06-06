package com.blogapp.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Value("${app.cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${app.cloudinary.api-key:}")
    private String apiKey;

    @Value("${app.cloudinary.api-secret:}")
    private String apiSecret;

    @Value("${app.cloudinary.folder:}")
    private String folder;

    private Cloudinary cloudinary;
    private boolean isConfigured = false;

    @PostConstruct
    public void init() {
        if (cloudName != null && !cloudName.isEmpty() && !cloudName.equals("placeholder") &&
            apiKey != null && !apiKey.isEmpty() && !apiKey.equals("placeholder") &&
            apiSecret != null && !apiSecret.isEmpty() && !apiSecret.equals("placeholder")) {
            
            try {
                this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret,
                    "secure", true
                ));
                this.isConfigured = true;
                System.out.println("=== CLOUDINARY INITIALIZED SUCCESSFULLY ===");
            } catch (Exception e) {
                System.err.println("=== FAILED TO INITIALIZE CLOUDINARY: " + e.getMessage() + " ===");
            }
        } else {
            System.out.println("=== CLOUDINARY NOT CONFIGURED: Uploads will be disabled ===");
        }
    }

    public boolean isConfigured() {
        return this.isConfigured;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (!isConfigured) {
            throw new IllegalStateException("Cloudinary is not configured.");
        }
        Map<String, Object> options = new java.util.HashMap<>();
        if (folder != null && !folder.isEmpty()) {
            options.put("folder", folder);
        }
        options.put("resource_type", "auto");
        options.put("use_filename", true);
        options.put("unique_filename", true);
        Map<?, ?> uploadResult = this.cloudinary.uploader().upload(file.getBytes(), options);
        return (String) uploadResult.get("secure_url");
    }
}
