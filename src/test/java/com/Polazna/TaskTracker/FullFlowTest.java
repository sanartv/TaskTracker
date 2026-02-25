package com.Polazna.TaskTracker;

import com.Polazna.TaskTracker.dto.CreateTaskRequest;
import com.Polazna.TaskTracker.dto.CreateUserRequest;
import com.Polazna.TaskTracker.dto.SigninRequest;
import com.Polazna.TaskTracker.entity.Role;
import com.Polazna.TaskTracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FullFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void fullUserScenario() throws Exception {
        CreateUserRequest signupRequest = new CreateUserRequest();
        signupRequest.setUsername("tester");
        signupRequest.setEmail("test@test.com");
        signupRequest.setPassword("password123");
        signupRequest.setRole(Role.User);

        System.out.println("Test: Sending, Register Request...");
        mockMvc.perform(post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());

        boolean exists = userRepository.existsByUsername("tester");
        System.out.println("TEST: User 'tester' exists in DB? -> " + exists);
        assertTrue(exists, "Пользователь должен был сохраниться в базе!");


        var user = userRepository.findByUsername("tester").get();
        System.out.println("TEST: DB Password Hash: " + user.getPassword());
        boolean matches = passwordEncoder.matches("password123", user.getPassword());
        System.out.println("TEST: Password matches? -> " + matches);
        assertTrue(matches, "Пароль в базе не совпадает с 'password123'");

        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUsername("tester");
        signinRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signinRequest)))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("TEST: Login Response Status: " + result.getResponse().getStatus());
        System.out.println("TEST: Login Response Body: " + result.getResponse().getContentAsString());

        if (result.getResponse().getStatus() != 200) {
            throw new RuntimeException("Login failed! See logs above.");
        }

        String responseContent = result.getResponse().getContentAsString();

        String token = objectMapper.readTree(responseContent).get("token").asText();
        Long userId = objectMapper.readTree(responseContent).get("id").asLong();

        System.out.println("Test token: " + token);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isForbidden());

        CreateTaskRequest taskRequest = new CreateTaskRequest();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("Testing....");
        taskRequest.setDeadline(LocalDateTime.now().plusDays(1));
        taskRequest.setAuthorId(userId);

        mockMvc.perform(post("/api/tasks")
                    .header("Authorization", "Bearer " +  token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }
}
