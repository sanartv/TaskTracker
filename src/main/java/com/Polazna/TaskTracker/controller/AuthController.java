package com.Polazna.TaskTracker.controller;

import com.Polazna.TaskTracker.dto.CreateUserRequest;
import com.Polazna.TaskTracker.dto.JwtResponse;
import com.Polazna.TaskTracker.dto.SigninRequest;
import com.Polazna.TaskTracker.entity.User;
import com.Polazna.TaskTracker.repository.UserRepository;
import com.Polazna.TaskTracker.security.JwtCore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest signUpRequest) {
       if (userRepository.existsByUsername(signUpRequest.getUsername())) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: Имя пользователя занято");
       }
       if(userRepository.existsByEmail(signUpRequest.getEmail())) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: Email уже занят");
       }

       User user = new User();
       user.setUsername(signUpRequest.getUsername());
       user.setEmail(signUpRequest.getEmail());
       user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
       user.setRole(signUpRequest.getRole());
       userRepository.save(user);

       return ResponseEntity.ok("Пользователь успешно зарегистрирован!");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Неверный логин или пароль", HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtCore.generateToken(authentication);

        User userDetails = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getRole().name()
        ));
    }
}
