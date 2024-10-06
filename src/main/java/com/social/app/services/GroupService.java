package com.social.app.services;

import com.social.app.exceptions.DuplicateEntityException;
import com.social.app.exceptions.InvalidInputException;
import com.social.app.models.User;
import com.social.app.repositories.GroupRepository;
import com.social.app.repositories.UserRepository;
import com.social.app.models.Group;
import com.social.app.validations.GenericValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
  private final GroupRepository groupRepository;
  private final UserRepository userRepository;
  private final GenericValidator genericValidator;

  public GroupService(
      GroupRepository groupRepository,
      UserRepository userRepository,
      GenericValidator genericValidator) {
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.genericValidator = genericValidator;
  }

  public Group createGroup(Group group) {
    validateNullFields(group);
    return groupRepository.save(group);
  }

  private void validateNullFields(Group group) {
    if (group.getName() == null || group.getName().isBlank()) {
      throw new InvalidInputException("Group name can't be null or empty");
    }
    if (group.getDescription() == null || group.getDescription().isBlank()) {
      throw new InvalidInputException("Group description can't be null or empty");
    }
  }

  @Transactional
  public void joinGroup(Long groupId, Long userId) {
    Group group =
        groupRepository
            .findById(groupId)
            .orElseThrow(() -> new EntityNotFoundException("Group not found"));
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    if (genericValidator.isUserInGroup(userId, groupId)) {
      throw new DuplicateEntityException(
          "User %s is already in group %s".formatted(userId, groupId));
    }
    group.getUsers().add(user);
    user.getGroups().add(group);
    groupRepository.save(group);
    userRepository.save(user);
  }

  public List<Group> getAllGroups() {
    return groupRepository.findAll();
  }

  @Transactional
  public void leaveGroup(Long groupId, Long userId) {
    Group group =
        groupRepository
            .findById(groupId)
            .orElseThrow(() -> new EntityNotFoundException("Group not found"));
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    if (!genericValidator.isUserInGroup(userId, groupId)) {
      throw new EntityNotFoundException("User %s is not in group %s".formatted(userId, groupId));
    }
    group.getUsers().remove(user);
    user.getGroups().remove(group);
    groupRepository.save(group);
    userRepository.save(user);
  }
}
