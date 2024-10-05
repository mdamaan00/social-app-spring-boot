package com.social.app.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.social.app.models.PostMeta;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostListingDto {
  private long id;
  private String content;
  private UserDto user;
  private long totalLikes;
  private long totalComments;
  private CommentDto latestComment;
  private boolean isLikedByUser;
  private LocalDateTime postedAt;

  public static PostListingDto map(PostMeta post) {
    return new PostListingDto(
        post.getId(),
        post.getContent(),
        UserDto.map(post.getUser()),
        post.getLikeCount(),
        post.getCommentCount(),
        CommentDto.map(post.getRecentComment()),
        post.getIsLikedByUser(),
        post.getCreatedAt());
  }
}
