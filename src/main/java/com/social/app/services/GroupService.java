package com.social.app.services;

import com.social.app.models.User;
import com.social.app.repositories.GroupRepository;
import com.social.app.repositories.UserRepository;
import com.social.app.models.Group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public Group createGroup(Group group) {
        // todo validate
        return groupRepository.save(group);
    }

    @Transactional
    public Group joinGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        group.getUsers().add(user);
        user.getGroups().add(group);
        groupRepository.save(group);
        userRepository.save(user);
        return group;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}
