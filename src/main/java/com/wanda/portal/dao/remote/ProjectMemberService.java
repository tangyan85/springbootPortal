package com.wanda.portal.dao.remote;

import com.wanda.portal.entity.ProjectMember;

/**
 * 项目成员服务
 *
 * @author cloud
 * @since 1.0.0 2018/5/21
 */
public interface ProjectMemberService {
    /**
     * 根据项目成员ID获取成员
     *
     * @param projectMemberId 项目成员ID
     * @return {@link ProjectMember}
     */
    ProjectMember findByProjectMemberId(Long projectMemberId);

    /**
     * 根据项目成员ID删除成员
     *
     * @param projectMemberId 项目成员ID
     */
    void deleteByProjectMemberId(Long projectMemberId);

}
