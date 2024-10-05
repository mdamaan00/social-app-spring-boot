package com.social.app.dtos;

import com.social.app.models.Post;
import com.social.app.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.social.app.models.Group;

@Data
@AllArgsConstructor
public class PostDto {
  private long id;
  private String content;
  private long userId;
  private long groupId;

  public Post toModel() {
    return Post.builder()
        .user(User.builder().id(userId).build())
        .group(Group.builder().id(groupId).build())
        .content(content)
        .build();
  }
}
