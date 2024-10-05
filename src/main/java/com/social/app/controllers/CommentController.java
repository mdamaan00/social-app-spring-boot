package com.social.app.controllers;

import com.social.app.models.Comment;
import com.social.app.dtos.CommentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.social.app.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping("")
  public ResponseEntity<Comment> addComment(@RequestBody CommentDto commentDto) {
    return ResponseEntity.ok(commentService.addComment(commentDto.toModel()));
  }

  @GetMapping("")
  public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable Long postId) {
    return ResponseEntity.ok(commentService.getAllComments(postId));
  }
}
