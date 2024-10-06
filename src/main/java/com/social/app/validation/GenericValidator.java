package com.social.app.validation;

import com.social.app.exception.UnprocessableEntityException;
import com.social.app.model.User;
import com.social.app.repository.PostRepository;
import com.social.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class GenericValidator {
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public GenericValidator(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public void checkIfPostPresent(Long postId) {
    postRepository
        .findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
  }

  public void isUserExistInGroup(Long userId, Long groupId) {
    boolean isInGroup = isUserInGroup(userId, groupId);
    if (!isInGroup) {
      throw new UnprocessableEntityException("User does not exist in group");
    }
  }

  public boolean isUserInGroup(Long userId, Long groupId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User does not exist"));
    return user.getGroups().stream().anyMatch(group -> group.getId().equals(groupId));
  }
}
