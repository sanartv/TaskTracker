package com.Polazna.TaskTracker.controller;

import com.Polazna.TaskTracker.dto.CreateTaskRequest;
import com.Polazna.TaskTracker.entity.Status;
import com.Polazna.TaskTracker.entity.Task;
import com.Polazna.TaskTracker.service.TaskService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Task create(@RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/author/{userId}")
    public List<Task> getTasksByAuthor(@PathVariable Long userId) {
        return taskService.getAllTasksByAuthorId(userId);
    }

    @PatchMapping("/{taskId}/status")
    public Task updateStatus(@PathVariable Long taskId, @RequestParam Status status) {
        return taskService.updateTaskStatus(taskId,status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
