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
import org.springframework.scheduling.annotation.AsyncResult;
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
    
//    @Async
    public Future<List<JiraProjectInputParam>> fetchAllJiras(List<Server> jiraServers) {
        List<JiraProjectInputParam> existJiras = new ArrayList<>();
        if (jiraServers != null && jiraServers.size() > 0) {
            for (Server jiraServer : jiraServers) {
                existJiras.addAll(jiraService.fetchUnusedJiraProject(jiraServer));
            }
        }
        return new AsyncResult<>(existJiras);
    }
    
//    @Async
    public Future<List<ConfluenceSpaceInputParam>> fetchAllConfluences(List<Server> confServers) {
        List<ConfluenceSpaceInputParam> existConfs = new ArrayList<>();
        if (confServers != null && confServers.size() > 0) {
            for (Server confServer : confServers) {
                existConfs.addAll(confluenceService.fetchUnusedConfs(confServer));
            }              
        }
        return new AsyncResult<>(existConfs);
    }
    
//    @Async
    public Future<List<JenkinsInputParam>> fetchAllJenkinses(List<Server> jenkinsServers) {
        List<JenkinsInputParam> existJenkinses = new ArrayList<>();
        if (jenkinsServers != null && jenkinsServers.size() > 0) {
            for (Server jenkinsServer : jenkinsServers) {
                existJenkinses.addAll(jenkinsService.fetchUnusedJekins(jenkinsServer));
            }
        }
        return new AsyncResult<>(existJenkinses);
    }
    
//    @Async
    public Future<List<ScmRepoInputParam>> fetchAllSvnRepos(List<Server> svnServers) {
        List<ScmRepoInputParam> existSvns = new ArrayList<>();
        if (svnServers != null && svnServers.size() > 0) {            
            for (Server svnServer : svnServers) {
                existSvns.addAll(repoService.fetchUnusedSvnRepos(svnServer));
            }              
        }
        return new AsyncResult<>(existSvns);
    }
    
//    @Async
    public Future<List<ScmRepoInputParam>> fetchAllSvnTemplates(List<Server> svnServers) {
        List<ScmRepoInputParam> svnTemplates = new ArrayList<>();
        if (svnServers != null && svnServers.size() > 0) {            
            for (Server svnServer : svnServers) {
                svnTemplates.addAll(repoService.fetchAllTemplates(svnServer));
            }              
        }
        return new AsyncResult<>(svnTemplates);
    }
}
