package com.social.app.controller;

import com.social.app.exception.DuplicateEntityException;
import com.social.app.exception.UnprocessableEntityException;
import com.social.app.dto.ApiResponse;
import com.social.app.model.Like;
import com.social.app.model.Post;
import com.social.app.model.User;
import com.social.app.service.LikeService;
import com.social.app.util.StatusMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups/{groupId}/posts/{postId}")
public class LikeController {

  private final LikeService likeService;

  public LikeController(LikeService likeService) {
    this.likeService = likeService;
  }

  @PostMapping("/like")
  public ResponseEntity<ApiResponse> like(
      @PathVariable Long postId, @RequestParam Long userId, @PathVariable Long groupId) {
    try {
      Like like =
          Like.builder()
              .post(Post.builder().id(postId).build())
              .user(User.builder().id(userId).build())
              .build();
      likeService.like(like, groupId);
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .message("Post liked successfully")
              .build());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    } catch (UnprocessableEntityException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    } catch (DuplicateEntityException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }

  @DeleteMapping("/dislike")
  public ResponseEntity<ApiResponse> dislike(
      @PathVariable Long postId, @RequestParam Long userId, @PathVariable Long groupId) {
    try {
      Like like =
          Like.builder()
              .post(Post.builder().id(postId).build())
              .user(User.builder().id(userId).build())
              .build();
      likeService.dislike(like, groupId);
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .message("Post disliked successfully")
              .build());
    } catch (UnprocessableEntityException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }
}
