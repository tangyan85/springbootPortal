package com.wanda.portal.dao.remote;
import java.util.List;
import java.util.Set;

import com.wanda.portal.entity.Artifact;

public interface ArtifactService {

	List<Artifact> findArtByprojectId(Long projectId);
}
