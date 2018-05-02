package com.wanda.portal.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanda.portal.entity.JiraProject;

public interface JiraProjectRepository extends JpaRepository<JiraProject, Long> {
	
	
}
