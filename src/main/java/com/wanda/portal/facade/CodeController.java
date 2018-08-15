package com.wanda.portal.facade;

import com.alibaba.fastjson.JSONArray;
import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dao.remote.RepoService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/code")
public class CodeController {
    private static final Logger logger = LoggerFactory.getLogger(CodeController.class);

    @Autowired
    ProjectService projectService;
    @Autowired
    AsyncTaskService asyncTaskService;
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    RepoService repoService;

    @RequestMapping("/toList")
    public String toList(Model model, String projectId) {
        model.addAttribute("projects", projectService.findAll(PageRequest.of(0, 10)));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        model.addAttribute("projectId", projectId);
        return "code/toList";
    }

    @RequestMapping("/remove")
    @ResponseBody
    public String remove(String repoId) {
        if (StringUtils.isNotEmpty(repoId)) {
           repoService.deleteByRepoId(Long.valueOf(repoId));
        }
        return "ok";
    }

    @RequestMapping(value="/switchType", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Server> switchType(String repoType) {
        return serverRepository.findByServerType(ServerType.find(RepoType.find(repoType)));
    }

    @RequestMapping(value="/switchServer", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<ScmRepoInputParam> switchServer(Long serverId, String repoType) {
        Server server = serverRepository.findById(serverId).get();
        return repoService.fetchScmByRepoType(server, RepoType.find(repoType));
    }

    @RequestMapping(value = "/preview/{projectId}")
    public String preview(Model model, @PathVariable("projectId") String projectId, String backPath) {
        Project project = new Project();
        try {
            if (StringUtils.isNotEmpty(projectId)) {
                project = projectService.getProjectById(Long.valueOf(projectId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Server> gitServer = serverRepository.findByServerType(ServerType.GIT);
        for (SCMRepo repo : project.getScmRepositories()) {
            for (Server server : gitServer) {
                if (repo.getWebui().contains(server.getOuterServerIpAndPort())) {
                    logger.info("repoRemoteId: " + repo.getRepoRemoteId());
                    JSONArray result = repoService.fetchAllBranches(server, repo.getRepoRemoteId());
                    repo.setBranches(result);
                }
            }
        }

        model.addAttribute("project", project);
        model.addAttribute("backPath", backPath);

        return "code/toDetail";
    }

    @RequestMapping(value = "/toAdd")
    public String toAdd(Model model, String projectId, String backPath) {
        setModelCommon(model);
        setCommonServerInfoAsync(model);
        Project project = new Project();
        try {
            if (StringUtils.isNotEmpty(projectId)) {
                project = projectService.getProjectById(Long.valueOf(projectId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("project", project);
        model.addAttribute("backPath", backPath);

        return "code/toAdd";
    }

    private void setModelCommon(Model model) {
        model.addAttribute("repoTypes", EnumSet.allOf(RepoType.class));
        model.addAttribute("inputActionTypes", EnumSet.allOf(InputActionType.class));
        model.addAttribute("serverIPs", serverRepository.findAll());
    }

    private void setCommonServerInfoAsync(Model model) {
        try {
            // 先从db获取svn的所有Server
            List<Server> svnServers = serverRepository.findByServerType(ServerType.SVN);
            // 轮询外部svn，过滤
            Future<List<ScmRepoInputParam>> existSvns = asyncTaskService.fetchAllSvnRepos(svnServers);
            // template似乎要去重
            Future<List<ScmRepoInputParam>> svnTemplates = asyncTaskService.fetchAllSvnTemplates(svnServers);

            model.addAttribute("existSvns", existSvns.get());
            model.addAttribute("svnTemplates", svnTemplates.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody save(@RequestBody ProjectInputParam projectInputParam,
                                       RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        Project project;
        try {
            project = projectService.findById(projectInputParam.getProjectId());
            projectService.createScm(projectInputParam.getScmRepositories(), project);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }

}
