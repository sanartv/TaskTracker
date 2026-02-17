package com.Polazna.TaskTracker.repository;

import com.Polazna.TaskTracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAuthorId(Long userId);

}
