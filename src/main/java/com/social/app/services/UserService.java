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
    validateNullFields(user);
    User userFromDb = userRepository.findByUsername(user.getUsername());
    if (userFromDb != null) {
      throw new RuntimeException("User already exists with same username");
    }
    return userRepository.save(user);
  }

  private void validateNullFields(User user) {
    if (user.getEmail() == null || user.getEmail().isBlank()) {
      throw new RuntimeException("Email can't be null or empty");
    }
    if (user.getUsername() == null || user.getUsername().isBlank()) {
      throw new RuntimeException("Username can't be null or empty");
    }
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}
