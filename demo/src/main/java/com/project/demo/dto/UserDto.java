package com.project.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    Integer id;
    String name;
    String email;
    String password;
    String role;
}
