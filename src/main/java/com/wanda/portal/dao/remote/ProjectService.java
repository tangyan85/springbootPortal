package com.wanda.portal.dao.remote;

import com.wanda.portal.entity.Project;
import com.wanda.portal.facade.model.input.*;
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

    List<Project> findByProjectNameLike(String projectName);

    void createScm(List<ScmRepoInputParam> list, Project proj) throws Exception;

    void createJira(List<JiraProjectInputParam> list, Project proj) throws Exception;

    void createConfluence(List<ConfluenceSpaceInputParam> list, Project proj) throws Exception;

    void createJenkins(List<JenkinsInputParam> list, Project proj) throws Exception;

    void createProjectMember(List<ProjectMemberInputParam> list, Project proj) throws Exception ;

    void createArtifact(List<ArtifactInputParam> list, Project proj) throws Exception;

    Project findById(Long projectId);
}
