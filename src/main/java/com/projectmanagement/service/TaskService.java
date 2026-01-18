package com.projectmanagement.service;

import jakarta.transaction.Transactional;
import com.projectmanagement.model.Task;
import org.springframework.stereotype.Service;
import com.projectmanagement.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> findByAssignedUser(Long userId) {
        return taskRepository.findByAssignedToId(userId);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public long countByStatus(Task.Status status) {
        return taskRepository.countByStatus(status);
    }

    public List<Task> findOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDate.now());
    }
}
