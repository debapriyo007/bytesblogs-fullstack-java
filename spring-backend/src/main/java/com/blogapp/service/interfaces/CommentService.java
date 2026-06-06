package com.blogapp.service.interfaces;

import com.blogapp.dto.request.CommentRequest;
import com.blogapp.dto.response.CommentResponse;
import com.blogapp.entity.User;
import java.util.List;

public interface CommentService {
    CommentResponse createComment(Long blogId, CommentRequest request, User user);
    CommentResponse updateComment(Long commentId, CommentRequest request, User user);
    void deleteComment(Long commentId, User user);
    List<CommentResponse> getCommentsByBlog(Long blogId);
}
