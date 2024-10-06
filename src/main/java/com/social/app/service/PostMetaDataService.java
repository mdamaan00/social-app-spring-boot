package com.social.app.service;

import com.social.app.model.Post;
import com.social.app.repository.CommentRepository;
import com.social.app.repository.LikeRepository;
import com.social.app.repository.PostRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PostMetaDataService {

  private final PostRepository postRepository;
  private final LikeRepository likeRepository;
  private final CommentRepository commentRepository;
  private final RedisTemplate<String, Long> redisTemplate;

  private static final long CACHE_EXPIRATION_TIME = 3600;

  public PostMetaDataService(
      PostRepository postRepository,
      LikeRepository likeRepository,
      CommentRepository commentRepository,
      RedisTemplate<String, Long> redisTemplate) {
    this.postRepository = postRepository;
    this.likeRepository = likeRepository;
    this.commentRepository = commentRepository;
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  public void init() {
    List<Post> posts = postRepository.findAll();
    loadLikeCounts(posts);
    loadCommentCounts(posts);
  }

  private void loadLikeCounts(List<Post> posts) {
    Map<Long, Long> likeCountMap = getLikeCountsByPostIds(posts.stream().map(Post::getId).toList());
    likeCountMap.forEach(
        (postId, likeCount) -> {
          String key = "post:like:" + postId;
          redisTemplate.opsForValue().set(key, likeCount);
          redisTemplate.expire(key, CACHE_EXPIRATION_TIME, TimeUnit.SECONDS);
        });
  }

  private void loadCommentCounts(List<Post> posts) {
    Map<Long, Long> commentCountMap =
        getCommentCountsByPostIds(posts.stream().map(Post::getId).toList());
    commentCountMap.forEach(
        (postId, commentCount) -> {
          String key = "post:comment:" + postId;
          redisTemplate.opsForValue().set(key, commentCount);
          redisTemplate.expire(key, CACHE_EXPIRATION_TIME, TimeUnit.SECONDS);
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
    String key = "post:like:" + postId;
    Long likeCount = redisTemplate.opsForValue().get(key);
    if (likeCount == null) {
      likeCount = likeRepository.countByPostId(postId);
      redisTemplate.opsForValue().set(key, likeCount);
      redisTemplate.expire(key, CACHE_EXPIRATION_TIME, TimeUnit.SECONDS);
    }
    return likeCount;
  }

  public Long getCommentCount(Long postId) {
    String key = "post:comment:" + postId;
    Long commentCount = redisTemplate.opsForValue().get(key);
    if (commentCount == null) {
      commentCount = commentRepository.countByPostId(postId);
      redisTemplate.opsForValue().set(key, commentCount);
      redisTemplate.expire(key, CACHE_EXPIRATION_TIME, TimeUnit.SECONDS);
    }
    return commentCount;
  }

  public void updateLikeCount(Long postId, boolean increment) {
    String key = "post:like:" + postId;
    Long currentLikeCount = redisTemplate.opsForValue().get(key);
    if (currentLikeCount == null) {
      currentLikeCount = 0L;
    }
    currentLikeCount += increment ? 1 : -1;
    redisTemplate.opsForValue().set(key, currentLikeCount);
    redisTemplate.expire(key, CACHE_EXPIRATION_TIME, TimeUnit.SECONDS);
  }

  public void updateCommentCount(Long postId, boolean increment) {
    String key = "post:comment:" + postId;
    Long currentCommentCount = redisTemplate.opsForValue().get(key);
    if (currentCommentCount == null) {
      currentCommentCount = 0L;
    }
    currentCommentCount += increment ? 1 : -1;
    redisTemplate.opsForValue().set(key, currentCommentCount);
    redisTemplate.expire(key, CACHE_EXPIRATION_TIME, TimeUnit.SECONDS);
  }
}
