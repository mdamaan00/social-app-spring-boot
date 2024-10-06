package com.social.app.repositories;

import com.social.app.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  @Query(
      "SELECT c FROM Comment c WHERE c.post.id IN :postIds AND c.createdAt = (SELECT MAX(c2.createdAt) FROM Comment c2 WHERE c2.post.id = c.post.id)")
  List<Comment> findMostRecentCommentsByPostIds(List<Long> postIds);

  @Query("SELECT c.post.id, COUNT(c) FROM Comment c WHERE c.post.id IN :postIds GROUP BY c.post.id")
  List<Object[]> findCommentCountsByPostIds(@Param("postIds") List<Long> postIds);

  @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
  Long countByPostId(@Param("postId") Long postId);

  List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
}
