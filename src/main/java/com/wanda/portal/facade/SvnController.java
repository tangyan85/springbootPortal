package com.wanda.portal.facade;

import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.impl.RepoServiceImpl;
import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.entity.Server;
import com.wanda.portal.utils.ConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/svn")
public class SvnController {
	@Autowired
	RepoServiceImpl repoServiceImpl;
	
	@Autowired
	ServerRepository serverRepository;

	@RequestMapping(value = "/findSvnByProjectId/{projectId}/{optype}")
	public String findSvnByProjectId(Model model,@PathVariable("projectId") String projectId,@PathVariable("optype") String optype) {
		List<SCMRepo> svns = repoServiceImpl.findSvnByProjectId(Long.valueOf(projectId));
		model.addAttribute("svns", svns);
		model=getServer(model);
		return "svn/findSvnById";
	}

	private Model getServer(Model model){
		Model m=model;
		List<Server> list = new ArrayList<>();
		list = serverRepository.findAll();
		for (Server s : list) {
			if(s.getServerType()==ServerType.VIEWVC){
				m.addAttribute(s.getServerType().toString() + "_SERVER",ConversionUtil.Con2ServerOutputParam(s));
			}
		}
		
		return m;
	}
	
}
