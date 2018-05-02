package com.wanda.portal.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wanda.portal.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
