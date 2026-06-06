package com.blogapp.controller;

import com.blogapp.dto.request.TagRequest;
import com.blogapp.dto.response.ApiResponse;
import com.blogapp.dto.response.TagResponse;
import com.blogapp.service.interfaces.TagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> createTag(@Valid @RequestBody TagRequest request) {
        TagResponse response = tagService.createTag(request);
        return ResponseEntity.ok(ApiResponse.success("Tag created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        List<TagResponse> response = tagService.getAllTags();
        return ResponseEntity.ok(ApiResponse.success("Tags fetched successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> getTagById(@PathVariable Long id) {
        TagResponse response = tagService.getTagById(id);
        return ResponseEntity.ok(ApiResponse.success("Tag fetched successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequest request) {
        TagResponse response = tagService.updateTag(id, request);
        return ResponseEntity.ok(ApiResponse.success("Tag updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(ApiResponse.success("Tag deleted successfully"));
    }
}
