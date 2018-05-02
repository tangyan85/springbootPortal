package com.wanda.portal.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wanda.portal.entity.SCMRepo;

public interface SCMRepoRepository extends JpaRepository<SCMRepo, Long> {
}
