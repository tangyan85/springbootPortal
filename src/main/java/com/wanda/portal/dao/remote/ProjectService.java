package com.wanda.portal.dao.remote;

import java.util.List;

import com.wanda.portal.entity.Project;
import com.wanda.portal.facade.model.input.ProjectInputParam;

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
}
