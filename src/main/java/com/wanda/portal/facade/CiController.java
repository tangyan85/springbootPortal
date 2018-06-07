package com.wanda.portal.facade;

import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.JenkinsService;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.JenkinsInputParam;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/ci")
public class CiController {
    @Autowired
    ProjectService projectService;
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    JenkinsService jenkinsService;
    @Autowired
    AsyncTaskService asyncTaskService;

    @RequestMapping("/toList")
    public String toList(Model model) {
        model.addAttribute("projects", projectService.findAll(PageRequest.of(0, 10)));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        return "ci/toList";
    }

    @RequestMapping(value = "/preview/{projectId}/{returnBtn}")
    public String preview(Model model, @PathVariable("projectId") String projectId,
                          @PathVariable("returnBtn") Boolean returnBtn) {
        Project project = new Project();
        try {
            project = projectService.getProjectById(Long.valueOf(projectId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("project", project);
        model.addAttribute("returnBtn", returnBtn);

        return "ci/toDetail";
    }

    @RequestMapping("/toAdd/{returnBtn}")
    public String toAdd(Model model, @PathVariable("returnBtn") Boolean returnBtn) {
        setModelCommon(model);
        setCommonServerInfoAsync(model);
        model.addAttribute("returnBtn", returnBtn);
        return "ci/toAdd";
    }

    private void setModelCommon(Model model) {
        model.addAttribute("inputActionTypes", EnumSet.allOf(InputActionType.class));
        model.addAttribute("serverIPs", serverRepository.findAll());
    }

    private void setCommonServerInfoAsync(Model model) {
        try {
            // 先从db中获取jenkins的所有Server
            List<Server> jenkinsServers = serverRepository.findByServerType(ServerType.JENKINS);
            // 再轮询外部jenkins，过滤
            Future<List<JenkinsInputParam>> existJenkinses = asyncTaskService.fetchAllJenkinses(jenkinsServers);

            model.addAttribute("existJenkins", existJenkinses.get());
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
            projectService.createJenkins(projectInputParam.getJenkinsProjects(), project);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }
}
