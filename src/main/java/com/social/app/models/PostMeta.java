package com.social.app.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostMeta {
  private Long id;
  private User user;
  private Group group;
  private String content;
  private Long likeCount;
  private Long commentCount;
  private Comment recentComment;
  private Boolean isLikedByUser;
  private LocalDateTime createdAt;

  public static PostMeta map(Post post) {
    return PostMeta.builder()
        .id(post.getId())
        .user(post.getUser())
        .group(post.getGroup())
        .content(post.getContent())
        .createdAt(post.getCreatedAt())
        .build();
  }
}
