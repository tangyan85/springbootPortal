package com.wanda.portal.facade;

import com.wanda.portal.constants.AritifactType;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.ArtifactService;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Artifact;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import com.wanda.portal.utils.ConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Controller
@RequestMapping("/artifact")
public class ArtifactController {
    @Autowired
    ServerRepository serverRepository;

    @Autowired
    ArtifactService artifactService;

    @Autowired
    ProjectService projectService;

    @RequestMapping("/toList")
    public String toList(Model model) {
        model.addAttribute("projects", projectService.findAll(PageRequest.of(0, 10)));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        return "artifact/toList";
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

        return "artifact/toDetail";
    }

    @RequestMapping("/toAdd/{returnBtn}")
    public String toAdd(Model model, @PathVariable("returnBtn") Boolean returnBtn) {
        setModelCommon(model);
        setCommonServerInfoAsync(model);
        model.addAttribute("returnBtn", returnBtn);
        return "artifact/toAdd";
    }

    private void setModelCommon(Model model) {
        model.addAttribute("aritifactTypes", EnumSet.allOf(AritifactType.class));
        model.addAttribute("serverIPs", serverRepository.findAll());
    }

    private void setCommonServerInfoAsync(Model model) {

    }

    @RequestMapping(value = "/findArtByProjectId/{projectId}/{optype}")
    public String findArtByProjectId(Model model, @PathVariable("projectId") String projectId, @PathVariable("optype") String optype) {
        List<Artifact> artifacts = new ArrayList<>();
        artifacts = (List<Artifact>) artifactService.findArtByprojectId(Long.valueOf(projectId));
        model.addAttribute("artifacts", artifacts);
        model = getServer(model);
        return "artifact/findArtById";
    }

    private Model getServer(Model model) {
        Model m = model;
        List<Server> list = new ArrayList<>();
        list = serverRepository.findAll();
        for (Server s : list) {
            if (s.getServerType() == ServerType.ARTIFACT) {
                m.addAttribute(s.getServerType().toString() + "_SERVER", ConversionUtil.Con2ServerOutputParam(s));
            }
        }

        return m;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody save(@RequestBody ProjectInputParam projectInputParam,
                                       RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        Project project;
        try {
            project = projectService.findById(projectInputParam.getProjectId());
            projectService.createArtifact(projectInputParam.getArtifacts(), project);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }

}
