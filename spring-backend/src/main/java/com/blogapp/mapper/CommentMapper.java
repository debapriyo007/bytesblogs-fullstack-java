package com.blogapp.mapper;

import com.blogapp.dto.response.CommentResponse;
import com.blogapp.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponse toResponse(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getUser() != null ? comment.getUser().getUsername() : null)
                .blogId(comment.getBlog() != null ? comment.getBlog().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
