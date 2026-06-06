package com.blogapp.service.impl;

import com.blogapp.dto.request.CommentRequest;
import com.blogapp.dto.response.CommentResponse;
import com.blogapp.entity.Blog;
import com.blogapp.entity.Comment;
import com.blogapp.entity.User;
import com.blogapp.enums.Role;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.exception.UnauthorizedException;
import com.blogapp.mapper.CommentMapper;
import com.blogapp.repository.BlogRepository;
import com.blogapp.repository.CommentRepository;
import com.blogapp.service.interfaces.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            BlogRepository blogRepository,
            CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentResponse createComment(Long blogId, CommentRequest request, User user) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + blogId));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .blog(blog)
                .user(user)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest request, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (user.getRole() != Role.ADMIN && !comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not have permission to update this comment");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toResponse(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (user.getRole() != Role.ADMIN && !comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByBlog(Long blogId) {
        if (!blogRepository.existsById(blogId)) {
            throw new ResourceNotFoundException("Blog not found with id: " + blogId);
        }
        return commentRepository.findByBlogId(blogId).stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
