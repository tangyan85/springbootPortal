package com.wanda.portal.facade;

import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.dto.jira.JiraProjectComponentDTO;
import com.wanda.portal.dto.jira.JiraProjectVersionDTO;
import com.wanda.portal.entity.JiraProject;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/task")
public class TaskController extends BaseController {
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

    @RequestMapping(value = "/component/{jiraProjectId}")
    @ResponseBody
    public List<JiraProjectComponentDTO> component(Model model, @PathVariable("jiraProjectId") String jiraProjectId) {
        JiraProject jiraProject = jiraService.findById(jiraProjectId);
        List<JiraProjectComponentDTO> components = null;

        if (jiraProject != null) {
            List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
            Server server = null;
            for (Server s : jiraServers) {
                if (jiraProject.getWebui().contains(s.getDomain())) {
                    server = s;
                    break;
                }
            }
            components = getJiraProjectComponentDTOS(jiraProject, server);
        }

        return components;
    }

    private List<JiraProjectComponentDTO> getJiraProjectComponentDTOS(JiraProject jiraProject, Server server) {
        String componentsKey = jiraProject.getJiraProjectKey() + "_components";
        return getForCache(componentsKey, () -> jiraService.fetchProjectComponents(jiraProject.getJiraProjectKey(), server));
    }

    @RequestMapping(value = "/preview/{projectId}")
    public String preview(Model model, @PathVariable("projectId") String projectId, String backPath) {
        Project project = new Project();
        try {
            if (StringUtils.isNotEmpty(projectId)) {
                project = projectService.getProjectById(Long.valueOf(projectId));
                if (project != null) {
                    Set<JiraProject> jiraProjects = project.getJiraProjects();
                    for (JiraProject jiraProject : jiraProjects) {
                        Server server = null;
                        List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
                        for (Server s : jiraServers) {
                            if (jiraProject.getWebui().contains(s.getDomain())) {
                                server = s;
                                break;
                            }
                        }
                        setJiraProject(jiraProject, server);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("project", project);
        model.addAttribute("backPath", backPath);

        return "task/toDetail";
    }

    private void setJiraProject(JiraProject jiraProject, Server server) {
        String allIssuesLink = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + "/issues/?jql=project %3D" + jiraProject.getJiraProjectKey();
        String finishIssuesLink = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + "/issues/?jql=project %3D" + jiraProject.getJiraProjectKey() + " AND status %3D 5";
        String versionLink = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + "/issues/?jql=project %3D" + jiraProject.getJiraProjectKey() + " AND affectedVersion  %3D ";

        jiraProject.setAllIssuesLink(allIssuesLink);
        jiraProject.setFinishIssuesLink(finishIssuesLink);
        String versionsKey = jiraProject.getJiraProjectKey() + "_versions";
        List<JiraProjectVersionDTO> versions = getForCache(versionsKey, () -> jiraService.fetchProjectVersions(jiraProject.getJiraProjectKey(), server));
        for (JiraProjectVersionDTO dto : versions) {
            dto.setUrl(versionLink + dto.getName());
        }
        jiraProject.setProjectVersions(versions);

        JiraProjectVersionDTO nextVersion = versions.stream().filter(t -> !t.getReleased()).min(Comparator.comparing(JiraProjectVersionDTO::getStartDate)).get();
        jiraProject.setNextVersion(nextVersion);

        String allIssuesKey = jiraProject.getJiraProjectKey() + "_allIssues";
        Integer allIssues = getForCache(allIssuesKey, () -> jiraService.fetchProjectAllIssues(jiraProject.getJiraProjectKey(), server));
        jiraProject.setAllIssues(allIssues);

        String finishIssuesKey = jiraProject.getJiraProjectKey() + "_finishIssues";
        Integer finishIssues = getForCache(finishIssuesKey, () -> jiraService.fetchProjectAllIssues(jiraProject.getJiraProjectKey(), server));
        jiraProject.setFinishIssues(finishIssues);
    }

    @RequestMapping(value = "/switchServer", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<JiraProjectInputParam> switchServer(Long serverId) {
        Server server = serverRepository.findById(serverId).get();
        return jiraService.fetchUnusedJiraProject(server);
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

        return "task/toAdd";
    }

    private void setModelCommon(Model model) {
        model.addAttribute("inputActionTypes", EnumSet.allOf(InputActionType.class));
        model.addAttribute("serverIPs", serverRepository.findAll());
    }

    private void setCommonServerInfoAsync(Model model) {
        try {
            // 先从db中获取jira的所有Server
            List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
            // 再轮询外部jira，过滤
            Future<List<JiraProjectInputParam>> existJiras = asyncTaskService.fetchAllJiras(jiraServers);

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
