package com.wanda.portal.dao.remote;
import com.wanda.portal.entity.Artifact;

import java.util.List;

public interface ArtifactService {

	List<Artifact> findArtByprojectId(Long projectId);

    void deleteByArtifactId(Long artifactId);
}
