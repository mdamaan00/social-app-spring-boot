package com.social.app.repositories;

import com.social.app.models.Like;
import com.social.app.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId")
    Long countByPostId(@Param("postId") Long postId);

    @Query("SELECT l.post FROM Like l WHERE l.user.id = :userId")
    List<Post> findPostsLikedByUser(@Param("userId") Long userId);
}
