package com.social.app.repositories;

import com.social.app.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    @Query("SELECT p FROM Post p WHERE p.group.id = :groupId ORDER BY p.createdAt DESC")
    List<Post> findPostsByGroupId(@Param("groupId") Long groupId);
}
