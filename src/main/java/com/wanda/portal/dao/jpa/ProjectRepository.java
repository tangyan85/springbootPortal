package com.wanda.portal.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wanda.portal.entity.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProjectNameLike(String projectName);
}
