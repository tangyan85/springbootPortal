package com.wanda.portal.dao.jpa;

import com.wanda.portal.entity.JenkinsProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JenkinsProjectRepository extends JpaRepository<JenkinsProject, Long> {
}
