package com.wanda.portal.dao.jpa;

import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProjectNameLike(String projectName);

    List<Project> findByProjectKey(String projectKey);

    List<Project> findByProjectName(String projectName);

    @Query("select p from Project p inner join ProjectMember pm on p.projectId = pm.project.projectId where pm.username = :username")
    List<Project> findByUsername(@Param("username") String username);

    @Query("select p from Project p left join ProjectMember pm on p.projectId = pm.project.projectId where pm.username = :username and p.status = :status")
    List<Project> findByUsername(@Param("username") String username, @Param("status") ProjectStatus status);

    List<Project> findByStatus(ProjectStatus status);

    @Query("select p from Project p left join ProjectMember pm on p.projectId = pm.project.projectId where p.projectName like :projectName and pm.username = :username")
    List<Project> findByUsernameAndProjectNameLike(@Param("projectName") String projectName, @Param("username") String username);

    @Query("select p.status, count(1) from Project p group by p.status")
    List<Object> aggregatePoject();
}
