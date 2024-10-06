package com.social.app.service;

import com.social.app.exception.InvalidInputException;
import com.social.app.model.Comment;
import com.social.app.model.Post;
import com.social.app.model.PostMeta;
import com.social.app.repository.PostRepository;
import com.social.app.validation.GenericValidator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final CommentService commentService;
  private final PostMetaDataService postMetaDataService;
  private final GenericValidator genericValidator;

  public PostService(
      PostRepository postRepository,
      CommentService commentService,
      PostMetaDataService postMetaDataService,
      GenericValidator genericValidator) {
    this.postRepository = postRepository;
    this.commentService = commentService;
    this.postMetaDataService = postMetaDataService;
    this.genericValidator = genericValidator;
  }

  public Post createPost(Post post) {
    validateNullFields(post);
    genericValidator.isUserExistInGroup(post.getUser().getId(), post.getGroup().getId());
    return postRepository.save(post);
  }

  public Post editPost(Post post) {
    validateNullFields(post);
    genericValidator.isUserExistInGroup(post.getUser().getId(), post.getGroup().getId());
    Post postFromDb =
        postRepository
            .findById(post.getId())
            .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
    validateNewPostWithOld(post, postFromDb);
    postFromDb.setContent(post.getContent());
    return postRepository.save(post);
  }

  private void validateNewPostWithOld(Post newPost, Post oldPost) {
    if (!newPost.getUser().getId().equals(oldPost.getUser().getId())) {
      throw new InvalidInputException(
          "User: %s can't edit Post: %s".formatted(newPost.getUser().getId(), oldPost.getId()));
    }
    if (!newPost.getGroup().getId().equals(oldPost.getGroup().getId())) {
      throw new InvalidInputException("Invalid group while editing post");
    }
  }

  private void validateNullFields(Post post) {
    if (post.getContent() == null || post.getContent().isBlank()) {
      throw new InvalidInputException("Post content can't be null or empty");
    }
    if (post.getUser() == null) {
      throw new InvalidInputException("User must be present in post");
    }
    if (post.getGroup() == null) {
      throw new InvalidInputException("Group must be present in post");
    }
  }

  public List<PostMeta> getAllPostsForGroup(Long groupId, Long userId) {
    genericValidator.isUserExistInGroup(userId, groupId);
    List<PostMeta> posts =
        postRepository.findPostsByGroupIdOrderByCreatedAtDesc(groupId).stream()
            .map(PostMeta::map)
            .toList();
    Set<Long> postsLikedByUser =
        postRepository.findPostsLikedByUser(userId).stream()
            .map(Post::getId)
            .collect(Collectors.toSet());
    Map<Long, Comment> recentCommentMap =
        commentService.getMostRecentCommentsByPostIds(posts.stream().map(PostMeta::getId).toList());
    posts.forEach(
        post -> {
          post.setRecentComment(recentCommentMap.get(post.getId()));
          post.setIsLikedByUser(postsLikedByUser.contains(post.getId()));
          post.setCommentCount(postMetaDataService.getCommentCount(post.getId()));
          post.setLikeCount(postMetaDataService.getLikeCount(post.getId()));
        });
    return posts;
  }
}
