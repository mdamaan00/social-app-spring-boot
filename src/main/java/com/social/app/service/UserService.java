package com.social.app.service;

import com.social.app.exception.DuplicateEntityException;
import com.social.app.exception.InvalidInputException;
import com.social.app.repository.UserRepository;
import com.social.app.model.User;
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
      throw new DuplicateEntityException("User already exists with same username");
    }
    return userRepository.save(user);
  }

  private void validateNullFields(User user) {
    if (user.getEmail() == null || user.getEmail().isBlank()) {
      throw new InvalidInputException("Email can't be null or empty");
    }
    if (user.getUsername() == null || user.getUsername().isBlank()) {
      throw new InvalidInputException("Username can't be null or empty");
    }
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}
