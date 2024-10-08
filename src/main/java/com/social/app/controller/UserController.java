package com.social.app.controller;

import com.social.app.dto.UserDto;
import com.social.app.exception.DuplicateEntityException;
import com.social.app.exception.InvalidInputException;
import com.social.app.dto.ApiResponse;
import com.social.app.service.UserService;
import com.social.app.util.StatusMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createUser(@RequestBody UserDto userDto) {
    try {
      return ResponseEntity.ok(
          ApiResponse.builder()
              .status(StatusMessage.OK)
              .data(UserDto.map(userService.createUser(userDto.toCreateModel())))
              .message("User created successfully")
              .build());
    } catch (DuplicateEntityException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    } catch (InvalidInputException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(ApiResponse.builder().status(StatusMessage.ERROR).message(e.getMessage()).build());
    }
  }

  @GetMapping
  public ResponseEntity<ApiResponse> getAllUsers() {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .status(StatusMessage.OK)
            .data(userService.getAllUsers().stream().map(UserDto::map).toList())
            .build());
  }
}
