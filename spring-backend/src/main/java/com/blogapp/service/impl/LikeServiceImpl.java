package com.blogapp.service.impl;

import com.blogapp.entity.Blog;
import com.blogapp.entity.Like;
import com.blogapp.entity.User;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.repository.BlogRepository;
import com.blogapp.repository.LikeRepository;
import com.blogapp.service.interfaces.LikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final BlogRepository blogRepository;

    public LikeServiceImpl(LikeRepository likeRepository, BlogRepository blogRepository) {
        this.likeRepository = likeRepository;
        this.blogRepository = blogRepository;
    }

    @Override
    public void likeBlog(Long blogId, User user) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + blogId));

        if (likeRepository.existsByBlogIdAndUserId(blogId, user.getId())) {
            return; // Idempotent
        }

        Like like = Like.builder()
                .blog(blog)
                .user(user)
                .build();

        likeRepository.save(like);
    }

    @Override
    public void unlikeBlog(Long blogId, User user) {
        if (!blogRepository.existsById(blogId)) {
            throw new ResourceNotFoundException("Blog not found with id: " + blogId);
        }

        likeRepository.findByBlogIdAndUserId(blogId, user.getId())
                .ifPresent(likeRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLikeCount(Long blogId) {
        if (!blogRepository.existsById(blogId)) {
            throw new ResourceNotFoundException("Blog not found with id: " + blogId);
        }
        return likeRepository.countByBlogId(blogId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLikedByCurrentUser(Long blogId, User user) {
        if (user == null) {
            return false;
        }
        if (!blogRepository.existsById(blogId)) {
            throw new ResourceNotFoundException("Blog not found with id: " + blogId);
        }
        return likeRepository.existsByBlogIdAndUserId(blogId, user.getId());
    }
}
