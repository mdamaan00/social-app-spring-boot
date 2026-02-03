package com.social.app.dto;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.social.app.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto2 {
  private long id;
  private String comment;
  private PostDto post;
  private UserDto user;
  private LocalDateTime commentedAt;

  public Comment toModel() {
    return Comment.builder().content(comment).post(post.toModel()).user(user.toModel()).build();
  }

  public static CommentDto2 map(Comment comment) {
    return Optional.ofNullable(comment)
        .map(
            cmt ->
                new CommentDto2(
                    cmt.getId(),
                    cmt.getContent(),
                    null,
                    UserDto.map(cmt.getUser()),
                    cmt.getCreatedAt()))
        .orElse(null);
  }
}
