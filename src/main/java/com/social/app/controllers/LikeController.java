package com.social.app.controllers;

import com.social.app.dtos.LikeDto;
import com.social.app.models.Like;
import com.social.app.services.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
public class LikeController {

  private final LikeService likeService;

  public LikeController(LikeService likeService) {
    this.likeService = likeService;
  }

  @PostMapping("")
  public ResponseEntity<Like> addLike(@RequestBody LikeDto likeDto) {
    return ResponseEntity.ok(likeService.addLike(likeDto.toModel()));
  }
}
