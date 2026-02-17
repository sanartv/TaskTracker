package com.Polazna.TaskTracker.service;

import com.Polazna.TaskTracker.dto.CreateUserRequest;
import com.Polazna.TaskTracker.entity.Role;
import com.Polazna.TaskTracker.entity.User;
import com.Polazna.TaskTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(CreateUserRequest request) {

        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существет");
        }
        if(userRepository.existsByEmail(request.getEmail())) {
           throw new RuntimeException("Email уже занят");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(request.getPassword());

        if (request.getRole() == null) {
            user.setRole(Role.User);
        } else {
            user.setRole(request.getRole());
        }

        return userRepository.save(user);
    }
}
