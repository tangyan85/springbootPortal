package com.wanda.portal.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.jpa.ArtifactsRepository;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.ArtifactService;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Artifact;
import com.wanda.portal.entity.Server;
import com.wanda.portal.utils.ConversionUtil;
import com.wanda.portal.utils.RedisUtil;

@Controller
@RequestMapping("/artifact")
public class ArtifactController {
	@Autowired
	ServerRepository serverRepository;
	
	@Autowired
	ArtifactService artifactService;

	@RequestMapping(value = "/findArtByProjectId/{projectId}/{optype}")
	public String findArtByProjectId(Model model,@PathVariable("projectId") String projectId,@PathVariable("optype") String optype) {
		List<Artifact> artifacts = new ArrayList<>();
		artifacts = (List<Artifact>) artifactService.findArtByprojectId(Long.valueOf(projectId));
		model.addAttribute("artifacts", artifacts);
		model=getServer(model);
		return "artifact/findArtById";
	}
	
	private Model getServer(Model model){
		Model m=model;
		List<Server> list = new ArrayList<>();
		list = serverRepository.findAll();
		for (Server s : list) {
			if(s.getServerType()==ServerType.ARTIFACT){
				m.addAttribute(s.getServerType().toString() + "_SERVER",ConversionUtil.Con2ServerOutputParam(s));
			}
		}
		
		return m;
	}

}
