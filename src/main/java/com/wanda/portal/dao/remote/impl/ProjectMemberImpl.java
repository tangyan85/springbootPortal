package com.wanda.portal.dao.remote.impl;

import com.wanda.portal.dao.jpa.ProjectMemberRepository;
import com.wanda.portal.dao.remote.ProjectMemberService;
import com.wanda.portal.entity.ProjectMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 项目成员服务实现
 *
 * @author cloud
 * @since 1.0.0 2018/5/21
 */
@Primary
@Service("projectMemberImpl")
public class ProjectMemberImpl implements ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Override
    public ProjectMember findByProjectMemberId(Long projectMemberId) {
        return projectMemberRepository.getOne(projectMemberId);
    }

    @Override
    public void deleteByProjectMemberId(Long projectMemberId) {
        projectMemberRepository.deleteById(projectMemberId);
    }
}
