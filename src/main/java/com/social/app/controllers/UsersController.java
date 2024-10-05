package com.social.app.controllers;

import com.social.app.dtos.UserDto;
import com.social.app.models.User;
import com.social.app.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
  private final UserService userService;

  public UsersController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
    return ResponseEntity.ok(userService.createUser(userDto.toModel()));
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers().stream().map(UserDto::map).toList());
  }
}
