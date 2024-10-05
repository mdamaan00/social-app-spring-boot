package com.social.app.dtos;

import com.social.app.models.Like;
import com.social.app.models.Post;
import com.social.app.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeDto {
  private long postId;

  private long userId;

  public Like toModel() {
    return Like.builder()
        .post(Post.builder().id(postId).build())
        .user(User.builder().id(userId).build())
        .build();
  }
}
