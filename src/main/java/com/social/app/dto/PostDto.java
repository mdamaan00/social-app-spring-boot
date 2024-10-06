package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.social.app.model.Group;
import com.social.app.model.Post;
import com.social.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {
  private long id;
  private String content;
  private UserDto user;
  private GroupDto group;

  public Post toModel() {
    return Post.builder()
        .id(id)
        .user(Optional.ofNullable(user).map(UserDto::toModel).orElse(null))
        .group(Optional.ofNullable(group).map(GroupDto::toModel).orElse(null))
        .content(content)
        .build();
  }

  public Post toCreateModel() {
    return Post.builder().user(user.toModel()).group(group.toModel()).content(content).build();
  }

  public static PostDto map(Post post) {
    return new PostDto(post.getId(), post.getContent(), null, null);
  }
}
