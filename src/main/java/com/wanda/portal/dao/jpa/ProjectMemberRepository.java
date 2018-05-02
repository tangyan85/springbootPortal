package com.wanda.portal.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wanda.portal.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
}
