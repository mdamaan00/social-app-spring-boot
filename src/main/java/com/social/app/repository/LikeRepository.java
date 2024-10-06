package com.social.app.repository;

import com.social.app.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
  @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId")
  boolean isPostLikedByUser(@Param("userId") Long userId, @Param("postId") Long postId);

  Like findLikeByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

  @Query("SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId")
  Long countByPostId(@Param("postId") Long postId);


  @Query("SELECT l.post.id, COUNT(l) FROM Like l WHERE l.post.id IN :postIds GROUP BY l.post.id")
  List<Object[]> findLikeCountsByPostIds(@Param("postIds") List<Long> postIds);
}
