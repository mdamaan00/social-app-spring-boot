package com.social.app.services;

import com.social.app.models.Like;
import com.social.app.models.Post;
import com.social.app.repositories.PostRepository;
import com.social.app.repositories.UserRepository;
import org.springframework.stereotype.Service;
import com.social.app.repositories.LikeRepository;

@Service
public class LikeService {
  private final LikeRepository likeRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostMetaDataService postMetaDataService;

  public LikeService(
      LikeRepository likeRepository,
      PostRepository postRepository,
      UserRepository userRepository,
      PostMetaDataService postMetaDataService) {
    this.likeRepository = likeRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.postMetaDataService = postMetaDataService;
  }

  public Like addLike(Like like) {
    userRepository
        .findById(like.getUser().getId())
        .orElseThrow(() -> new RuntimeException("User not found"));
    Post post =
        postRepository
            .findById(like.getPost().getId())
            .orElseThrow(() -> new RuntimeException("Post not found"));
    Like response = likeRepository.save(like);
    postMetaDataService.updateLikeCount(post.getId(), true);
    return response;
  }
}
