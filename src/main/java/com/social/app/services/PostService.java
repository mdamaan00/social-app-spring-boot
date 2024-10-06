package com.social.app.services;

import com.social.app.exceptions.InvalidInputException;
import com.social.app.models.Comment;
import com.social.app.models.Post;
import com.social.app.models.PostMeta;
import com.social.app.repositories.PostRepository;
import com.social.app.validations.GenericValidator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

  private void validateNullFields(Post post) {
    if (post.getContent() == null || post.getContent().isBlank()) {
      throw new InvalidInputException("Post content can't be null or empty");
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
