package com.blogapp.service.interfaces;

import com.blogapp.entity.User;

public interface LikeService {
    void likeBlog(Long blogId, User user);
    void unlikeBlog(Long blogId, User user);
    long getLikeCount(Long blogId);
    boolean isLikedByCurrentUser(Long blogId, User user);
}
