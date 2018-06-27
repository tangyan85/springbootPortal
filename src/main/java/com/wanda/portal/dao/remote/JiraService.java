package com.wanda.portal.dao.remote;

import com.wanda.portal.dto.jira.GenericJiraProjectDTO;
import com.wanda.portal.dto.jira.JiraInputDTO;
import com.wanda.portal.dto.jira.JiraOutputDTO;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JiraService {

    /* 采用内置6种模板创建，支持预先检验 */
    JiraOutputDTO createJiraProjectUsingTemplate(JiraInputDTO dto, boolean isCheckExist) throws Exception;

    /* 采用已有项目Id拷贝创建，支持预先校验 
     * 返回了Jira Project的内部Long id，这个id无大用
     * */
    Long createJiraProjectUsingExistingProject(Long existingProjectId, String key, String leader,
            String projectName, boolean isCheckExist) throws Exception;

    /*
     * jira可能是ProjectId或key,这里只暴露key的check
     * */
    boolean checkIfJiraProjectExist(String jiraKey) throws Exception;

    List<GenericJiraProjectDTO> fetchAllJiraProjects(UserDetails user);

    /**
     * 根据jirakey进行过滤
     * @return
     */
	List<JiraProjectInputParam> fetchUnusedJiraProject(UserDetails user);

	String loginJira(String username, String password) throws Exception;

    void setServer(Server server);
    
    Server gerServer();

    void deleteByJiraId(Long jiraId);
}
