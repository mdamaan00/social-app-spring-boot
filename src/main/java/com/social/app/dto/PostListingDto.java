package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.social.app.model.PostMeta;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostListingDto {
  private long id;
  private String content;
  private UserDto postedBy;
  private long totalLikes;
  private long totalComments;
  private CommentDto latestComment;
  private boolean isLikedByUser;
  private LocalDateTime postedAt;
  private LocalDateTime updatedAt;

  public static PostListingDto map(PostMeta post) {
    return new PostListingDto(
        post.getId(),
        post.getContent(),
        UserDto.map(post.getUser()),
        post.getLikeCount(),
        post.getCommentCount(),
        CommentDto.map(post.getRecentComment()),
        post.getIsLikedByUser(),
        post.getCreatedAt(),
        post.getUpdatedAt());
  }
}
