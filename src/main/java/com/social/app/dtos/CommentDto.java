package com.social.app.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.social.app.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
  private long id;
  private String comment;
  private PostDto post;
  private UserDto user;
  private LocalDateTime commentedAt;

  public Comment toModel() {
    return Comment.builder().content(comment).post(post.toModel()).user(user.toModel()).build();
  }

  public static CommentDto map(Comment comment) {
    return Optional.ofNullable(comment)
        .map(
            cmt ->
                new CommentDto(
                    cmt.getId(),
                    cmt.getContent(),
                    null,
                    UserDto.map(cmt.getUser()),
                    cmt.getCreatedAt()))
        .orElse(null);
  }
}
