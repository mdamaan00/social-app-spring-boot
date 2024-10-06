package com.social.app.service;

import com.social.app.exception.DuplicateEntityException;
import com.social.app.model.Like;
import com.social.app.validation.GenericValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.social.app.repository.LikeRepository;

import java.util.Optional;

@Service
public class LikeService {
  private final LikeRepository likeRepository;
  private final PostMetaDataService postMetaDataService;
  private final GenericValidator genericValidator;

  public LikeService(
      LikeRepository likeRepository,
      PostMetaDataService postMetaDataService,
      GenericValidator genericValidator) {
    this.likeRepository = likeRepository;
    this.postMetaDataService = postMetaDataService;
    this.genericValidator = genericValidator;
  }

  public void like(Like like, Long groupId) {
    genericValidator.checkIfPostPresent(like.getPost().getId());
    genericValidator.isUserExistInGroup(like.getUser().getId(), groupId);
    boolean alreadyLiked =
        likeRepository.isPostLikedByUser(like.getUser().getId(), like.getPost().getId());
    if (alreadyLiked) {
      throw new DuplicateEntityException("User has already liked this");
    }
    likeRepository.save(like);
    postMetaDataService.updateLikeCount(like.getPost().getId(), true);
  }

  public void dislike(Like like, Long groupId) {
    genericValidator.checkIfPostPresent(like.getPost().getId());
    genericValidator.isUserExistInGroup(like.getUser().getId(), groupId);
    Like response =
        likeRepository.findLikeByUserIdAndPostId(like.getUser().getId(), like.getPost().getId());
    Optional.ofNullable(response)
        .orElseThrow(() -> new EntityNotFoundException("This post was not liked"));
    likeRepository.deleteById(response.getId());
    postMetaDataService.updateLikeCount(like.getPost().getId(), false);
  }
}
