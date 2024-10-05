package com.social.app.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.social.app.models.Group;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDto {
  private long id;
  private String name;
  private String description;

  public Group toModel() {
    return Group.builder().id(id).name(name).description(description).build();
  }

  public Group toCreateModel() {
    return Group.builder().name(name).description(description).build();
  }

  public static GroupDto map(Group group) {
    return new GroupDto(group.getId(), group.getName(), group.getDescription());
  }
}
