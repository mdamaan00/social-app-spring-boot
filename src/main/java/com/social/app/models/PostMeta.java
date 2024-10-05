package com.social.app.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
