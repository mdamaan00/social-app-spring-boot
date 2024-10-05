package com.social.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.social.app.models.Group;

@Data
@AllArgsConstructor
public class GroupDto {
  private long id;
  private String name;
  private String description;

  public Group toModel() {
    return Group.builder().name(name).description(description).build();
  }

  public static GroupDto map(Group group) {
    return new GroupDto(group.getId(), group.getName(), group.getDescription());
  }
}
