package com.social.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.social.app.models.PostMeta;

@Data
@AllArgsConstructor
public class PostListingDto {
  private long id;
  private String content;
  private UserDto user;
  private long likeCount;
  private long commentCount;
  private boolean isLikedByUser;

  public static PostListingDto map(PostMeta post) {
    return new PostListingDto(
        post.getId(),
        post.getContent(),
        UserDto.map(post.getUser()),
        post.getLikeCount(),
        post.getCommentCount(),
        post.getIsLikedByUser());
  }
}
