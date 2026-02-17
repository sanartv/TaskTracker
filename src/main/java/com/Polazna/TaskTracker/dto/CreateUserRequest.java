package com.Polazna.TaskTracker.dto;

import com.Polazna.TaskTracker.entity.Role;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String email;
    private Role role;
}
