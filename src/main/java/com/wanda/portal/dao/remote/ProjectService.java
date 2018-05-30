package com.wanda.portal.dao.remote;

import com.wanda.portal.entity.Project;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProjectService {

    public Project getProjectByKey(String projectKey);

    @Deprecated
    public void createProject(Project project) throws Exception;
    @Deprecated
    public void updateProject(Project project);

    public void addExistingProjectPortal(Project project);

    public List<Project> findAllByRankDesc();

    public Project getProjectById(Long projectId);

    public Project createProject(ProjectInputParam projectInputParam) throws Exception;
    
    public Project updateProject(ProjectInputParam projectInputParam) throws Exception;

	String endProject(Long projectId);

    Page<Project> findAll(PageRequest page);
}
