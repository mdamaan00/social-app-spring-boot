package com.social.app.services;

import com.social.app.models.Post;
import com.social.app.repositories.CommentRepository;
import com.social.app.repositories.LikeRepository;
import com.social.app.repositories.PostRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class PostMetaDataService {

  private static final Map<Long, Long> postLikeCountMap = new ConcurrentHashMap<>();
  private static final Map<Long, Long> commentCountMap = new ConcurrentHashMap<>();

  private final PostRepository postRepository;
  private final LikeRepository likeRepository;
  private final CommentRepository commentRepository;

  public PostMetaDataService(
      PostRepository postRepository,
      LikeRepository likeRepository,
      CommentRepository commentRepository) {
    this.postRepository = postRepository;
    this.likeRepository = likeRepository;
    this.commentRepository = commentRepository;
  }

  @PostConstruct
  public void init() {
    loadLikeCounts();
    loadCommentCounts();
  }

  private void loadLikeCounts() {
    List<Post> posts = postRepository.findAll();
    posts.forEach(
        post -> {
          Long likeCount = likeRepository.countByPostId(post.getId());
          postLikeCountMap.put(post.getId(), likeCount);
        });
  }

  private void loadCommentCounts() {
    List<Post> posts = postRepository.findAll();
    posts.forEach(
        post -> {
          Long commentCount = commentRepository.countByPostId(post.getId());
          commentCountMap.put(post.getId(), commentCount);
        });
  }

  public Long getLikeCount(Long postId) {
    return postLikeCountMap.getOrDefault(postId, 0L);
  }

  public Long getCommentCount(Long postId) {
    return commentCountMap.getOrDefault(postId, 0L);
  }

  public synchronized void updateLikeCount(Long postId, boolean increment) {
    postLikeCountMap.put(postId, postLikeCountMap.getOrDefault(postId, 0L) + (increment ? 1 : -1));
  }

  public synchronized void updateCommentCount(Long postId, boolean increment) {
    commentCountMap.put(postId, commentCountMap.getOrDefault(postId, 0L) + (increment ? 1 : -1));
  }
}
