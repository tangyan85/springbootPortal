package com.wanda.portal.facade;

import com.wanda.portal.constants.ProjectMemberRole;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.dao.jpa.UserRepository;
import com.wanda.portal.dao.remote.ProjectMemberService;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.User;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.EnumSet;
import java.util.List;

@RequestMapping("/member")
@Controller
public class MemberController {
    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectMemberService projectMemberService;
    @Autowired
    UserRepository userRepository;

    @RequestMapping("/toList")
    public String toList(Model model, String projectId) {
        model.addAttribute("projects", projectService.findAll(PageRequest.of(0, 10)));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        model.addAttribute("projectId", projectId);
        return "member/toList";
    }

    @ResponseBody
    @RequestMapping(value = "/pull", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<User> pull(String search) {
        return userRepository.findByUserKeyLike("%" + search + "%");
    }

    @RequestMapping("/remove")
    @ResponseBody
    public String remove(String projectMemberId) {
        if (StringUtils.isNotEmpty(projectMemberId)) {
            projectMemberService.deleteByProjectMemberId(Long.valueOf(projectMemberId));
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

        return "member/toAdd";
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
