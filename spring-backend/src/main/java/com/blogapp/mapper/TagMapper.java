package com.blogapp.mapper;

import com.blogapp.dto.request.TagRequest;
import com.blogapp.dto.response.TagResponse;
import com.blogapp.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagResponse toResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public Tag toEntity(TagRequest request) {
        if (request == null) {
            return null;
        }
        return Tag.builder()
                .name(request.getName())
                .build();
    }
}
