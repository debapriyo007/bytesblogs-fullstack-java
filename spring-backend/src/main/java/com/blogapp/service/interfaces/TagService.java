package com.blogapp.service.interfaces;

import com.blogapp.dto.request.TagRequest;
import com.blogapp.dto.response.TagResponse;
import java.util.List;

public interface TagService {
    TagResponse createTag(TagRequest request);
    List<TagResponse> getAllTags();
    TagResponse getTagById(Long id);
    TagResponse updateTag(Long id, TagRequest request);
    void deleteTag(Long id);
}
