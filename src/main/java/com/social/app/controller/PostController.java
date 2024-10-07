package com.social.app.controller;

import com.social.app.dto.ApiResponse;
import com.social.app.dto.GroupDto;
import com.social.app.dto.PostDto;
import com.social.app.dto.PostListingDto;
import com.social.app.exception.InvalidInputException;
import com.social.app.exception.UnprocessableEntityException;
import com.social.app.service.PostService;
import com.social.app.util.StatusMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups/{groupId}/posts")
public class PostController {
  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createPost(
      @RequestBody PostDto postDto, @PathVariable Long groupId) {
    try {
      postDto.setGroup(new GroupDto(groupId, null, null));
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .data(PostDto.map(postService.createPost(postDto.toCreateModel())))
              .message("Post created successfully")
              .build());
    } catch (InvalidInputException | UnprocessableEntityException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }

  @PutMapping("/{postId}")
  public ResponseEntity<ApiResponse> editPost(
      @RequestBody PostDto postDto, @PathVariable Long groupId, @PathVariable Long postId) {
    try {
      postDto.setGroup(new GroupDto(groupId, null, null));
      postDto.setId(postId);
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .data(PostDto.map(postService.editPost(postDto.toModel())))
              .message("Post edited successfully")
              .build());
    } catch (InvalidInputException | UnprocessableEntityException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getPostsForGroup(
      @PathVariable Long groupId, @RequestParam Long userId) {
    try {
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .data(
                  postService.getAllPostsForGroup(groupId, userId).stream()
                      .map(PostListingDto::map)
                      .toList())
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
