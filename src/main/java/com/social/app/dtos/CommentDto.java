package com.social.app.dtos;

import com.social.app.models.Comment;
import com.social.app.models.Post;
import com.social.app.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {

  private String comment;

  private Long postId;

  private Long userId;

  public Comment toModel() {
    return Comment.builder().content(comment).post(Post.builder().id(postId).build()).user(User.builder().id(userId).build()).build();
  }
}
