package com.Polazna.TaskTracker.service;

import com.Polazna.TaskTracker.dto.CreateTaskRequest;
import com.Polazna.TaskTracker.entity.Status;
import com.Polazna.TaskTracker.entity.Task;
import com.Polazna.TaskTracker.entity.User;
import com.Polazna.TaskTracker.exception.ResourceNotFoundException;
import com.Polazna.TaskTracker.repository.TaskRepository;
import com.Polazna.TaskTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<Task> getAllTasksByAuthorId(Long userId) {
        return taskRepository.findByAuthorId(userId);
    }

    public Task createTask(CreateTaskRequest request) {
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setStatus(Status.NEW);
        task.setAuthor(author);

        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Long taskId, Status newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!task.getAuthor().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Вы не можете изменить чужую задачу!");
        }

        task.setStatus(newStatus);

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с ID: " + id + " не найдена"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!task.getAuthor().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Вы не можете удалить чужую задачу!");
        }

        taskRepository.delete(task);
    }


    public List<Task> getTasksByStatus(Long userId, Status status) {
        return taskRepository.findByAuthorIdAndStatus(userId, status);
    }

    public List<Task> searchTasks(Long userId, String keyword) {
        return taskRepository.findByAuthorIdAndTitleContainingIgnoreCase(userId,keyword);
    }

    public List<Task> getTasksSortedByDeadline(Long userId) {
        return taskRepository.findByAuthorIdOrderByDeadlineAsc(userId);
    }

}
