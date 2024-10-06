package com.social.app.controllers;

import com.social.app.dtos.PostDto;
import com.social.app.exceptions.InvalidInputException;
import com.social.app.exceptions.UnprocessableEntityException;
import com.social.app.models.ApiResponse;
import com.social.app.dtos.CommentDto;

import com.social.app.utils.StatusMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.social.app.services.CommentService;

@RestController
@RequestMapping("/groups/{groupId}/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addComment(
      @RequestBody CommentDto commentDto, @PathVariable Long postId, @PathVariable Long groupId) {
    try {
      commentDto.setPost(new PostDto(postId, null, null, null));
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .data(CommentDto.map(commentService.addComment(commentDto.toModel(), groupId)))
              .message("Commented successfully")
              .build());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    } catch (InvalidInputException | UnprocessableEntityException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<ApiResponse> deleteComment(
      @PathVariable Long commentId,
      @PathVariable Long postId,
      @PathVariable Long groupId,
      @RequestParam Long userId) {
    try {
      commentService.deleteComment(commentId, postId, groupId, userId);
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .message("Comment deleted successfully")
              .build());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    } catch (InvalidInputException | UnprocessableEntityException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getCommentsForPost(
      @PathVariable Long postId, @PathVariable Long groupId) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .status(StatusMessage.OK)
            .data(commentService.getAllComments(postId).stream().map(CommentDto::map).toList())
            .build());
  }
}
