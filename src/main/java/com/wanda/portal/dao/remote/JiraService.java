package com.wanda.portal.dao.remote;

import com.alibaba.fastjson.JSONArray;
import com.wanda.portal.dto.jira.*;
import com.wanda.portal.entity.JiraProject;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;

import java.util.List;

public interface JiraService {

    /* 采用内置6种模板创建，支持预先检验 */
    JiraOutputDTO createJiraProjectUsingTemplate(JiraInputDTO dto, boolean isCheckExist, Server server) throws Exception;

    /* 采用已有项目Id拷贝创建，支持预先校验 
     * 返回了Jira Project的内部Long id，这个id无大用
     * */
    Long createJiraProjectUsingExistingProject(Long existingProjectId, String key, String leader,
            String projectName, boolean isCheckExist, Server server) throws Exception;

    /*
     * jira可能是ProjectId或key,这里只暴露key的check
     * */
    boolean checkIfJiraProjectExist(String jiraKey, Server server) throws Exception;

    List<GenericJiraProjectDTO> fetchAllJiraProjects(Server server);

    /**
     * 根据jirakey进行过滤
     * @return
     */
	List<JiraProjectInputParam> fetchUnusedJiraProject(Server server);

	String loginJira(String username, String password, Server server) throws Exception;

    void deleteByJiraId(Long jiraId);

    List<JiraProjectVersionDTO> fetchProjectVersions(final String projectId, Server server);

    List<JiraProjectComponentDTO> fetchProjectComponents(final String projectId, Server server);

    JiraProject findById(String jiraProjectId);

    Integer fetchProjectAllIssues(final String projectId, Server server);

    Integer fetchProjectFinishIssues(final String projectId, Server server);

    JSONArray fetchAllToDos(Server s);

    JSONArray fetchAllDones(Server server);

    List<JiraProject> findAll();
}
