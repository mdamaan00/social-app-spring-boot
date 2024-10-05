package com.social.app.controllers;

import com.social.app.dtos.GroupDto;
import com.social.app.models.Group;
import com.social.app.services.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
  private final GroupService groupService;

  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  @PostMapping
  public ResponseEntity<Group> createGroup(@RequestBody GroupDto groupDto) {
    return ResponseEntity.ok(groupService.createGroup(groupDto.toModel()));
  }

  @PostMapping("/{groupId}/join/{userId}")
  public ResponseEntity<Group> joinGroup(@PathVariable Long groupId, @PathVariable Long userId) {
    return ResponseEntity.ok(groupService.joinGroup(groupId, userId));
  }

  @GetMapping
  public ResponseEntity<List<GroupDto>> getAllGroups() {
    return ResponseEntity.ok(groupService.getAllGroups().stream().map(GroupDto::map).toList());
  }
}
