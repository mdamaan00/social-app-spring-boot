package com.social.app.repositories;

import com.social.app.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findPostsByGroupIdOrderByCreatedAtDesc(Long groupId);

  @Query("SELECT l.post FROM Like l WHERE l.user.id = :userId")
  List<Post> findPostsLikedByUser(@Param("userId") Long userId);
}
