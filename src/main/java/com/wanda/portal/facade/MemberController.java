package com.wanda.portal.facade;

import com.wanda.portal.constants.ProjectMemberRole;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Project;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.EnumSet;

@RequestMapping("/member")
@Controller
public class MemberController {
    @Autowired
    ProjectService projectService;

    @RequestMapping("/toList")
    public String toList(Model model) {
        model.addAttribute("projects", projectService.findAll(PageRequest.of(0, 10)));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        return "member/toList";
    }

    @RequestMapping("/toAdd/{returnBtn}")
    public String toAdd(Model model, @PathVariable("returnBtn") Boolean returnBtn) {
        setModelCommon(model);
        setCommonServerInfoAsync(model);
        model.addAttribute("returnBtn", returnBtn);
        return "member/toAdd";
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

        return "member/toDetail";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody save(@RequestBody ProjectInputParam projectInputParam,
                                       RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        Project project;
        try {
            project = projectService.findById(projectInputParam.getProjectId());
            projectService.createProjectMember(projectInputParam.getProjectMembers(), project);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }

    private void setModelCommon(Model model) {
        model.addAttribute("projectMemberRoles", EnumSet.allOf(ProjectMemberRole.class));
    }

    private void setCommonServerInfoAsync(Model model) {

    }
}
