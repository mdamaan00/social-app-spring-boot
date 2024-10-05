package com.social.app.controllers;

import com.social.app.dtos.GroupDto;
import com.social.app.models.ApiResponse;
import com.social.app.services.GroupService;
import com.social.app.utils.StatusMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupController {
  private final GroupService groupService;

  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createGroup(@RequestBody GroupDto groupDto) {
    try {
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .data(GroupDto.map(groupService.createGroup(groupDto.toCreateModel())))
              .message("Group created successfully")
              .build());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }

  @PostMapping("/{groupId}/join/users/{userId}")
  public ResponseEntity<ApiResponse> joinGroup(
      @PathVariable Long groupId, @PathVariable Long userId) {
    try {
      groupService.joinGroup(groupId, userId);
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .message("User: %s joined group: %s successfully".formatted(userId, groupId))
              .build());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getAllGroups() {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .status(StatusMessage.OK)
            .data(groupService.getAllGroups().stream().map(GroupDto::map).toList())
            .build());
  }
}
