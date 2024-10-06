package com.social.app.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

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
  private LocalDateTime updatedAt;

  public static PostMeta map(Post post) {
    return PostMeta.builder()
        .id(post.getId())
        .user(post.getUser())
        .group(post.getGroup())
        .content(post.getContent())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
