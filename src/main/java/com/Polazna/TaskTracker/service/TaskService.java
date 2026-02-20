package com.Polazna.TaskTracker.service;

import com.Polazna.TaskTracker.dto.CreateTaskRequest;
import com.Polazna.TaskTracker.entity.Status;
import com.Polazna.TaskTracker.entity.Task;
import com.Polazna.TaskTracker.entity.User;
import com.Polazna.TaskTracker.repository.TaskRepository;
import com.Polazna.TaskTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

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
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        task.setStatus(newStatus);

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
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
