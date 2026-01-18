package com.projectmanagement.repository;


import com.projectmanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStatus(Project.Status status);

    @Query("SELECT p FROM Project p JOIN p.teamMembers tm WHERE tm.id = :userId")
    List<Project> findByTeamMemberId(Long userId);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    long countByStatus(Project.Status status);
}
