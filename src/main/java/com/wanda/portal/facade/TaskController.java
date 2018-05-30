package com.wanda.portal.facade;

import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.EnumSet;

@Controller
@RequestMapping("/task")
public class TaskController {
	@Autowired
	ProjectService projectService;

	@RequestMapping("/toList")
	public String toList(Model model) {
		model.addAttribute("projects", projectService.findAll(PageRequest.of(1, 10)));
		model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
		return "task/toList";
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

		return "task/toDetail";
	}

	@RequestMapping("/toAdd")
	public String toAdd(Model model){
		return "task/toAdd";
	}
}
