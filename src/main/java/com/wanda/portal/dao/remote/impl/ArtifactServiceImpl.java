package com.wanda.portal.dao.remote.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.wanda.portal.dao.jpa.ProjectRepository;
import com.wanda.portal.dao.remote.ArtifactService;
import com.wanda.portal.entity.Artifact;
import com.wanda.portal.entity.Project;

@Primary
@Service("ArtifactServiceImpl")
public class ArtifactServiceImpl implements ArtifactService {
	@Autowired
	ProjectRepository projectRepository;
	@Override
	public List<Artifact> findArtByprojectId(Long projectId){
		List<Artifact> ret=new ArrayList<>();
		Project project=projectRepository.findById(projectId).get();
		ret.addAll(project.getArtifacts());
		return ret;
	}

}
