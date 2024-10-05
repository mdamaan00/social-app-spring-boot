package com.social.app.controllers;

import com.social.app.dtos.PostDto;
import com.social.app.dtos.PostListingDto;
import com.social.app.models.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.social.app.services.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  public ResponseEntity<Post> createPost(@RequestBody PostDto postDto) {
    return ResponseEntity.ok(postService.createPost(postDto.toModel()));
  }

  @GetMapping("/groups/{groupId}")
  public ResponseEntity<List<PostListingDto>> getPostsForGroup(
      @PathVariable Long groupId, @RequestParam Long userId) {
    return ResponseEntity.ok(
        postService.getAllPostsForGroup(groupId, userId).stream()
            .map(PostListingDto::map)
            .toList());
  }
}
