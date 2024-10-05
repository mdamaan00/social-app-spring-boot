package com.social.app.services;

import com.social.app.models.Comment;
import com.social.app.models.Post;
import com.social.app.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import com.social.app.repositories.PostRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final PostMetaDataService postMetaDataService;

  public CommentService(PostRepository postRepository, CommentRepository commentRepository, PostMetaDataService postMetaDataService) {
      this.postRepository = postRepository;
      this.commentRepository = commentRepository;
      this.postMetaDataService = postMetaDataService;
  }

  public Map<Long, Comment> getMostRecentCommentsByPostIds(List<Long> postIds) {
    List<Comment> recentComments = commentRepository.findMostRecentCommentsByPostIds(postIds);
    return recentComments.stream()
        .collect(Collectors.toMap(comment -> comment.getPost().getId(), comment -> comment));
  }

  public Comment addComment(Comment comment)
  {
    Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(() -> new RuntimeException("Post does not exist"));
    Comment response = commentRepository.save(comment);
    postMetaDataService.updateCommentCount(post.getId(),true);
    return response;
  }

  public List<Comment> getAllComments(Long postId)
  {
    postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Comment not found"));
    return commentRepository.findByPostId(postId);

  }
}
