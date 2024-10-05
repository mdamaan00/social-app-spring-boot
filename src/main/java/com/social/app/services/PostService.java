package com.social.app.services;

import com.social.app.models.Comment;
import com.social.app.models.Post;
import com.social.app.models.PostMeta;
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
  private final UserService userService;
  private final CommentService commentService;
  private final PostMetaDataService postMetaDataService;

  public PostService(
      PostRepository postRepository,
      UserService userService,
      LikeRepository likeRepository,
      CommentService commentService,
      PostMetaDataService postMetaDataService) {
    this.postRepository = postRepository;
    this.userService = userService;
    this.likeRepository = likeRepository;
    this.commentService = commentService;
    this.postMetaDataService = postMetaDataService;
  }

  public Post createPost(Post post) {
    userService.isUserExistInGroup(post.getUser().getId(), post.getGroup().getId());
    return postRepository.save(post);
  }

  public List<PostMeta> getAllPostsForGroup(Long groupId, Long userId) {
    userService.isUserExistInGroup(userId, groupId);
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
          if (recentCommentMap.containsKey(post.getId())) {
            post.setRecentComment(recentCommentMap.get(post.getId()));
          }
          post.setIsLikedByUser(postsLikedByUser.contains(post.getId()));
        });
    return posts;
  }
}
