package com.blogapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private long fileSize;
}
