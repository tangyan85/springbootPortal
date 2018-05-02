package com.wanda.portal.dao.jpa;

import com.wanda.portal.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactsRepository extends JpaRepository<Artifact, Long> {
}
