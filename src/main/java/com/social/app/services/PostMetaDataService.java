package com.social.app.services;

import com.social.app.models.Post;
import com.social.app.repositories.CommentRepository;
import com.social.app.repositories.LikeRepository;
import com.social.app.repositories.PostRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    List<Post> posts = postRepository.findAll();
    loadLikeCounts(posts);
    loadCommentCounts(posts);
  }

  private void loadLikeCounts(List<Post> posts) {
    Map<Long, Long> likeCount = getLikeCountsByPostIds(posts.stream().map(Post::getId).toList());
    posts.forEach(
        post -> {
          postLikeCountMap.put(post.getId(), likeCount.getOrDefault(post.getId(), 0L));
        });
  }

  private void loadCommentCounts(List<Post> posts) {
    Map<Long, Long> commentCount =
        getCommentCountsByPostIds(posts.stream().map(Post::getId).toList());
    posts.forEach(
        post -> {
          commentCountMap.put(post.getId(), commentCount.getOrDefault(post.getId(), 0L));
        });
  }

  private Map<Long, Long> getCommentCountsByPostIds(List<Long> postIds) {
    List<Object[]> results = commentRepository.findCommentCountsByPostIds(postIds);
    return results.stream()
        .collect(Collectors.toMap(result -> (Long) result[0], result -> (Long) result[1]));
  }

  private Map<Long, Long> getLikeCountsByPostIds(List<Long> postIds) {
    List<Object[]> results = likeRepository.findLikeCountsByPostIds(postIds);
    return results.stream()
        .collect(Collectors.toMap(result -> (Long) result[0], result -> (Long) result[1]));
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
