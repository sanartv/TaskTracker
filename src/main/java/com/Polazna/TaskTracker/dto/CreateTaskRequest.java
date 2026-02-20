package com.Polazna.TaskTracker.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;


    private String description;

    @NotNull(message = "Дедлайн должен быть указан")
    @Future(message = "Дедлайн должен быть в будущем")
    private LocalDateTime deadline;

    @NotNull(message = "ID автора обязателен")
    private Long authorId;
}
