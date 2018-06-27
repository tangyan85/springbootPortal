package com.wanda.portal.dao;

import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.ConfluenceService;
import com.wanda.portal.dao.remote.JenkinsService;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dao.remote.RepoService;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;
import com.wanda.portal.facade.model.input.JenkinsInputParam;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
      异步线程池任务
 */
@Service
public class AsyncTaskService {
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    JiraService jiraService;
    @Autowired
    ConfluenceService confluenceService;
    @Autowired
    JenkinsService jenkinsService;
    @Autowired
    RepoService repoService;
    
    @Async
    public Future<List<JiraProjectInputParam>> fetchAllJiras(List<Server> jiraServers, UserDetails user) {
        List<JiraProjectInputParam> existJiras = new ArrayList<>();
        if (jiraServers != null && jiraServers.size() > 0) {
            for (Server jiraServer : jiraServers) {
                jiraService.setServer(jiraServer);
                existJiras.addAll(jiraService.fetchUnusedJiraProject(user));
            }
        }
        return new AsyncResult<>(existJiras);
    }
    
    @Async
    public Future<List<ConfluenceSpaceInputParam>> fetchAllConfluences(List<Server> confServers ) {       
        List<ConfluenceSpaceInputParam> existConfs = new ArrayList<>();
        if (confServers != null && confServers.size() > 0) {
            for (Server confServer : confServers) {
                confluenceService.setServer(confServer);
                existConfs.addAll(confluenceService.fetchUnusedConfs());
            }              
        }
        // model.addAttribute("existConfs", existConfs);
        return new AsyncResult<List<ConfluenceSpaceInputParam>>(existConfs);
    }
    
    @Async
    public Future<List<JenkinsInputParam>> fetchAllJenkinses(List<Server> jenkinsServers) {
        List<JenkinsInputParam> existJenkinses = new ArrayList<>();
        if (jenkinsServers != null && jenkinsServers.size() > 0) {
            for (Server jenkinsServer : jenkinsServers) {
                jenkinsService.setServer(jenkinsServer);
                existJenkinses.addAll(jenkinsService.fetchUnusedJekins());
            }
        }
        // model.addAttribute("existJenkins", existJenkinses);
        return new AsyncResult<List<JenkinsInputParam>>(existJenkinses);
    }
    
    @Async
    public Future<List<ScmRepoInputParam>> fetchAllSvnRepos(List<Server> svnServers) {
        List<ScmRepoInputParam> existSvns = new ArrayList<>();
        if (svnServers != null && svnServers.size() > 0) {            
            for (Server svnServer : svnServers) {
                repoService.setServer(svnServer);
                existSvns.addAll(repoService.fetchUnusedSvnRepos());
            }              
        }
        // model.addAttribute("existSvns", existSvns);
        return new AsyncResult<List<ScmRepoInputParam>>(existSvns); 
    }
    
    @Async
    public Future<List<ScmRepoInputParam>> fetchAllSvnTemplates(List<Server> svnServers) {
        List<ScmRepoInputParam> svnTemplates = new ArrayList<>();
        if (svnServers != null && svnServers.size() > 0) {            
            for (Server svnServer : svnServers) {
                repoService.setServer(svnServer);
                svnTemplates.addAll(repoService.fetchAllTemplates());
            }              
        }
        // model.addAttribute("svnTemplates", svnTemplates);
        return new AsyncResult<List<ScmRepoInputParam>>(svnTemplates);
    }
}
