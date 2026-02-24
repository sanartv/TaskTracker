package com.Polazna.TaskTracker.service;

import com.Polazna.TaskTracker.dto.CreateUserRequest;
import com.Polazna.TaskTracker.entity.Role;
import com.Polazna.TaskTracker.entity.User;
import com.Polazna.TaskTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, UserDetailsPasswordService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    public User getCurrentUser() {
        return null;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, @Nullable String newPassword) {
        return null;
    }
}
