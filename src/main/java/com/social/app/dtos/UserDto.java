package com.social.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.social.app.models.User;

@Data
@AllArgsConstructor
public class UserDto {
  private long id;
  private String userName;
  private String email;

  public User toModel() {
    return User.builder().username(userName).email(email).build();
  }

  public static UserDto map(User user) {
    return new UserDto(user.getId(), user.getUsername(), user.getEmail());
  }
}
