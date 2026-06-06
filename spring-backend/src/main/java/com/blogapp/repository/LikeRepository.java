package com.blogapp.repository;

import com.blogapp.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByBlogIdAndUserId(Long blogId, Long userId);
    long countByBlogId(Long blogId);
    Optional<Like> findByBlogIdAndUserId(Long blogId, Long userId);
}
