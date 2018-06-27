package com.wanda.portal.dao.remote.impl;

import com.wanda.portal.dao.jpa.ArtifactsRepository;
import com.wanda.portal.dao.jpa.ProjectRepository;
import com.wanda.portal.dao.remote.ArtifactService;
import com.wanda.portal.entity.Artifact;
import com.wanda.portal.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Primary
@Service("ArtifactServiceImpl")
public class ArtifactServiceImpl implements ArtifactService {
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	ArtifactsRepository rrtifactsRepository;

	@Override
	public List<Artifact> findArtByprojectId(Long projectId){
		List<Artifact> ret=new ArrayList<>();
		Project project=projectRepository.findById(projectId).get();
		ret.addAll(project.getArtifacts());
		return ret;
	}

	@Override
	public void deleteByArtifactId(Long artifactId) {
		rrtifactsRepository.deleteById(artifactId);
	}

}
