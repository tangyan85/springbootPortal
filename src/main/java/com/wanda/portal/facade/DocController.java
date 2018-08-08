package com.wanda.portal.facade;

import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.ConfluenceService;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.ConfluenceSpace;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/doc")
public class DocController extends BaseController {
    @Autowired
    ProjectService projectService;
    @Autowired
    ConfluenceService confluenceService;
    @Autowired
    AsyncTaskService asyncTaskService;
    @Autowired
    ServerRepository serverRepository;

    @RequestMapping("/toList")
    public String toList(Model model, String projectId) {
        model.addAttribute("projects", projectService.findAll(PageRequest.of(0, 10)));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        model.addAttribute("projectId", projectId);
        return "doc/toList";
    }

    @RequestMapping(value = "/preview/{projectId}")
    public String preview(Model model, @PathVariable("projectId") String projectId, String backPath) {
        Project project = new Project();
        try {
            if (StringUtils.isNotEmpty(projectId)) {
                project = projectService.getProjectById(Long.valueOf(projectId));
                if (project != null) {
                    Set<ConfluenceSpace> confProjects = project.getConfluenceSpaces();
                    for (ConfluenceSpace confProject : confProjects) {
                        Server server = null;
                        List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE);
                        for (Server s : confServers) {
                            if (confProject.getWebui().contains(s.getDomain())) {
                                server = s;
                                break;
                            }
                        }
                        setConfProject(confProject, server);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("project", project);
        model.addAttribute("backPath", backPath);

        return "doc/toDetail";
    }

    private void setConfProject(ConfluenceSpace confProject, Server server) {
        String allPagesLink, createPagesLink, modifyPagesLink;
        allPagesLink = server.getProtocol() + "://" + server.getDomain()
                + "/collector/pages.action?key=" + confProject.getSpaceKey();
        createPagesLink = server.getProtocol() + "://" + server.getDomain()
                + "/dosearchsite.action?cql=space+%3D+\"" + confProject.getSpaceKey() + "\"+and+created+>%3D+now(%27-1d%27)";
        modifyPagesLink = server.getProtocol() + "://" + server.getDomain()
                + "/dosearchsite.action?cql=space+%3D+\"" + confProject.getSpaceKey() + "\"+and+lastmodified+>%3D+now(%27-1d%27)";
        confProject.setAllPagesLink(allPagesLink);
        confProject.setCreatePagesLink(createPagesLink);
        confProject.setModifyPagesLink(modifyPagesLink);

        String allPagesKey = confProject.getSpaceKey() + "_allPages";
        Integer allPages = getForCache(allPagesKey, () -> confluenceService.fetchAllPages(confProject.getSpaceKey(), server));
        confProject.setAllPages(allPages);

        String createPagesKey = confProject.getSpaceKey() + "_createPages";
        Integer createPages = getForCache(createPagesKey, () -> confluenceService.fetchAllPagesByCreated(confProject.getSpaceKey(), server));
        confProject.setCreatePages(createPages);

        String modifyPagesKey = confProject.getSpaceKey() + "_modifyPages";
        Integer modifyPages = getForCache(modifyPagesKey, () -> confluenceService.fetchAllPagesByModified(confProject.getSpaceKey(), server));
        confProject.setModifyPages(modifyPages);
    }

    @RequestMapping("/remove")
    @ResponseBody
    public String remove(String confluenceId) {
        if (StringUtils.isNotEmpty(confluenceId)) {
            confluenceService.deleteByConfluenceId(Long.valueOf(confluenceId));
        }
        return "ok";
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

        return "doc/toAdd";
    }

    private void setModelCommon(Model model) {
        model.addAttribute("inputActionTypes", EnumSet.allOf(InputActionType.class));
        model.addAttribute("serverIPs", serverRepository.findAll());
    }

    private void setCommonServerInfoAsync(Model model) {
        try {
            // 先从db中获取confluence的所有Server
            List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE);
            // 再轮询外部confluence，过滤
            Future<List<ConfluenceSpaceInputParam>> existConfs = asyncTaskService.fetchAllConfluences(confServers);

            model.addAttribute("existConfs", existConfs.get());
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
            projectService.createConfluence(projectInputParam.getConfluenceSpaces(), project);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }
}
