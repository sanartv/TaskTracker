package com.Polazna.TaskTracker.repository;

import com.Polazna.TaskTracker.entity.Status;
import com.Polazna.TaskTracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAuthorId(Long userId);

    List<Task> findByAuthorIdAndStatus(Long userId, Status status);

    List<Task> findByAuthorIdAndTitleContainingIgnoreCase(Long userId, String titleKeyword);

    List<Task> findByAuthorIdOrderByDeadlineAsc(Long userId);

}
