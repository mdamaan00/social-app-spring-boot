package com.social.app.services;

import com.social.app.repositories.UserRepository;
import com.social.app.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(User user) {
    // todo validation
    return userRepository.save(user);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public void isUserExistInGroup(Long userId, Long groupId) {
    User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User does not exist"));
    boolean isInGroup = user.getGroups().stream().anyMatch(group -> group.getId().equals(groupId));

    if (!isInGroup) {
      throw new RuntimeException("User or group does not exist.");
    }
  }
}
