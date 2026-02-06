package com.project.demo.dto;

import com.project.demo.entity.User;
import lombok.*;

import java.util.List;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReqRes {
    private int userId;
    private int StatusCode;
    private String message;
    private String error;
    private String name;
    private String password;
    private String role;
    private String email;
    private User user;
    private String token;
    private String expirationTime;
    private List<UserDto> userList;
}
