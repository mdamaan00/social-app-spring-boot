package com.social.app.service;

import com.social.app.exception.InvalidInputException;
import com.social.app.model.Comment;
import com.social.app.repository.CommentRepository;
import com.social.app.validation.GenericValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {
  private final GenericValidator genericValidator;
  private final CommentRepository commentRepository;
  private final PostMetaDataService postMetaDataService;

  public CommentService(
      GenericValidator genericValidator,
      CommentRepository commentRepository,
      PostMetaDataService postMetaDataService) {
    this.genericValidator = genericValidator;
    this.commentRepository = commentRepository;
    this.postMetaDataService = postMetaDataService;
  }

  public Map<Long, Comment> getMostRecentCommentsByPostIds(List<Long> postIds) {
    List<Comment> recentComments = commentRepository.findMostRecentCommentsByPostIds(postIds);
    return recentComments.stream()
        .collect(Collectors.toMap(comment -> comment.getPost().getId(), comment -> comment));
  }

  public Comment addComment(Comment comment, Long groupId) {
    genericValidator.checkIfPostPresent(comment.getPost().getId());
    genericValidator.isUserExistInGroup(comment.getUser().getId(), groupId);
    genericValidator.isPostInGroup(comment.getPost().getId(), groupId);
    Comment response = commentRepository.save(comment);
    postMetaDataService.updateCommentCount(comment.getPost().getId(), true);
    return response;
  }

  public void deleteComment(Long commentId, Long postId, Long groupId, Long userId) {
    genericValidator.isUserExistInGroup(userId, groupId);
    genericValidator.isPostInGroup(postId, groupId);
    Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("Comment doesn't exists"));
    if (!comment.getUser().getId().equals(userId)) {
      throw new InvalidInputException("User %s can't delete this comment".formatted(userId));
    }
    commentRepository.deleteById(commentId);
    postMetaDataService.updateCommentCount(postId, false);
  }

  public List<Comment> getAllComments(Long postId) {
    return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
  }
}
