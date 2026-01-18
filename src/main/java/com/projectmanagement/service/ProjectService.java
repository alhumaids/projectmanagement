package com.projectmanagement.service;

import jakarta.transaction.Transactional;
import com.projectmanagement.model.Project;
import org.springframework.stereotype.Service;
import com.projectmanagement.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findByStatus(Project.Status status) {
        return projectRepository.findByStatus(status);
    }

    public List<Project> findByTeamMember(Long userId) {
        return projectRepository.findByTeamMemberId(userId);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public long countByStatus(Project.Status status) {
        return projectRepository.countByStatus(status);
    }

    public long countActive() {
        return projectRepository.countByStatus(Project.Status.IN_PROGRESS);
    }
}
