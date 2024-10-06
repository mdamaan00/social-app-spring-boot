package com.social.app.controllers;

import com.social.app.dtos.GroupDto;
import com.social.app.dtos.PostDto;
import com.social.app.dtos.PostListingDto;
import com.social.app.exceptions.InvalidInputException;
import com.social.app.exceptions.UnprocessableEntityException;
import com.social.app.models.ApiResponse;
import com.social.app.utils.StatusMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.social.app.services.PostService;

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
