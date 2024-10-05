package com.social.app.services;

import com.social.app.models.Comment;
import com.social.app.models.Post;
import com.social.app.models.PostMeta;
import com.social.app.validations.GenericValidator;
import org.springframework.stereotype.Service;
import com.social.app.repositories.LikeRepository;
import com.social.app.repositories.PostRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final LikeRepository likeRepository;
  private final CommentService commentService;
  private final PostMetaDataService postMetaDataService;
  private final GenericValidator genericValidator;

  public PostService(
      PostRepository postRepository,
      LikeRepository likeRepository,
      CommentService commentService,
      PostMetaDataService postMetaDataService,
      GenericValidator genericValidator) {
    this.postRepository = postRepository;
    this.likeRepository = likeRepository;
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
      throw new RuntimeException("Post content can't be null or empty");
    }
  }

  public List<PostMeta> getAllPostsForGroup(Long groupId, Long userId) {
    genericValidator.isUserExistInGroup(userId, groupId);
    List<PostMeta> posts =
        postRepository.findPostsByGroupId(groupId).stream()
            .map(
                post ->
                    PostMeta.builder()
                        .id(post.getId())
                        .user(post.getUser())
                        .group(post.getGroup())
                        .content(post.getContent())
                        .commentCount(postMetaDataService.getCommentCount(post.getId()))
                        .likeCount(postMetaDataService.getLikeCount(post.getId()))
                        .createdAt(post.getCreatedAt())
                        .build())
            .toList();
    Set<Long> postsLikedByUser =
        likeRepository.findPostsLikedByUser(userId).stream()
            .map(Post::getId)
            .collect(Collectors.toSet());
    Map<Long, Comment> recentCommentMap =
        commentService.getMostRecentCommentsByPostIds(posts.stream().map(PostMeta::getId).toList());
    posts.forEach(
        post -> {
          post.setRecentComment(recentCommentMap.getOrDefault(post.getId(), null));
          post.setIsLikedByUser(postsLikedByUser.contains(post.getId()));
        });
    return posts;
  }
}
