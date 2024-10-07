package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.social.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
  private long id;
  private String userName;
  private String email;

  public User toModel() {
    return User.builder().id(id).username(userName).email(email).build();
  }

  public User toCreateModel() {
    return User.builder().username(userName).email(email).build();
  }

  public static UserDto map(User user) {
    return new UserDto(user.getId(), user.getUsername(), user.getEmail());
  }
}
