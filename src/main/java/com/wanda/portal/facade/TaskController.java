package com.wanda.portal.facade;

import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import com.wanda.portal.security.SecurityConfigCrowd;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    ProjectService projectService;
    @Autowired
    JiraService jiraService;
    @Autowired
    AsyncTaskService asyncTaskService;
    @Autowired
    ServerRepository serverRepository;

    @RequestMapping("/toList")
    public String toList(Model model, String projectId) {
        model.addAttribute("projects", projectService.findAll(PageRequest.of(0, 10)));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        model.addAttribute("projectId", projectId);
        return "task/toList";
    }

    @RequestMapping("/remove")
    @ResponseBody
    public String remove(String jiraId) {
        if (StringUtils.isNotEmpty(jiraId)) {
            jiraService.deleteByJiraId(Long.valueOf(jiraId));
        }
        return "ok";
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
        model.addAttribute("project", project);
        model.addAttribute("backPath", backPath);

        return "task/toDetail";
    }

    @RequestMapping(value="/switchServer", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<JiraProjectInputParam> switchServer(Long serverId, HttpSession session) {
        Server server = serverRepository.findById(serverId).get();
        jiraService.setServer(server);
        UserDetails user = (UserDetails) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
        return jiraService.fetchUnusedJiraProject(user);
    }

    @RequestMapping(value = "/toAdd")
    public String toAdd(Model model, String projectId, String backPath, HttpSession session) {
        setModelCommon(model);
        UserDetails user = (UserDetails) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
        setCommonServerInfoAsync(model, user);
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

        return "task/toAdd";
    }

    private void setModelCommon(Model model) {
        model.addAttribute("inputActionTypes", EnumSet.allOf(InputActionType.class));
        model.addAttribute("serverIPs", serverRepository.findAll());
    }

    private void setCommonServerInfoAsync(Model model, UserDetails user) {
        try {
            // 先从db中获取jira的所有Server
            List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
            // 再轮询外部jira，过滤
            Future<List<JiraProjectInputParam>> existJiras = asyncTaskService.fetchAllJiras(jiraServers, user);

            model.addAttribute("existJiras", existJiras.get());
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
            projectService.createJira(projectInputParam.getJiraProjects(), project);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }
}
