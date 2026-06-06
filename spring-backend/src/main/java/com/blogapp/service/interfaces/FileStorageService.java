package com.blogapp.service.interfaces;

import com.blogapp.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileResponse storeFile(MultipartFile file);
}
