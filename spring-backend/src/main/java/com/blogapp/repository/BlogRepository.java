package com.blogapp.repository;

import com.blogapp.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);
    
    @Query(value = "SELECT DISTINCT b.* FROM blogs_tbl b " +
                   "LEFT JOIN categories_tbl c ON b.category_id = c.id " +
                   "LEFT JOIN blog_tags bt ON b.id = bt.blog_id " +
                   "LEFT JOIN tags_tbl t ON bt.tag_id = t.id " +
                   "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                   "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                   "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                   "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))",
           countQuery = "SELECT COUNT(DISTINCT b.id) FROM blogs_tbl b " +
                        "LEFT JOIN categories_tbl c ON b.category_id = c.id " +
                        "LEFT JOIN blog_tags bt ON b.id = bt.blog_id " +
                        "LEFT JOIN tags_tbl t ON bt.tag_id = t.id " +
                        "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))",
           nativeQuery = true)
    Page<Blog> searchBlogsNative(@Param("keyword") String keyword, Pageable pageable);

    Page<Blog> findByCategoryId(Long categoryId, Pageable pageable);

    @Modifying
    @Query("UPDATE Blog b SET b.category = null WHERE b.category.id = :categoryId")
    void unsetCategoryForBlogs(@Param("categoryId") Long categoryId);

    @Modifying
    @Query(value = "DELETE FROM blog_tags WHERE tag_id = :tagId", nativeQuery = true)
    void deleteTagAssociations(@Param("tagId") Long tagId);
}
