package com.wanda.portal.facade.model.input;

import java.io.Serializable;
import java.util.Date;
import com.mysql.jdbc.StringUtils;
import com.wanda.portal.constants.ProjectMemberRole;
import com.wanda.portal.exception.CreateProjectValidationException;

public class ProjectMemberInputParam extends AbstractInputParam implements Serializable {

    private static final long serialVersionUID = 809003795988213548L;
    private Long projectMemberId;

    private String username;

    private ProjectMemberRole role;

    private Date createTime;
    
    public Long getProjectMemberId() {
        return projectMemberId;
    }

    public void setProjectMemberId(Long projectMemberId) {
        this.projectMemberId = projectMemberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ProjectMemberRole getRole() {
        return role;
    }

    public void setRole(ProjectMemberRole role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public InputParam validateCreate() throws Exception {
        if (this.role == null) {
            throw new CreateProjectValidationException("Member role should be valid!");
        }
        if (StringUtils.isNullOrEmpty(this.username)) {
            throw new CreateProjectValidationException("Member username should be valid!");
        }
        return this;
    }

    @Override
    public InputParam validateModify() throws Exception {
        if (this.role == null) {
            throw new CreateProjectValidationException("Member role should be valid!");
        }
        if (StringUtils.isNullOrEmpty(this.username)) {
            throw new CreateProjectValidationException("Member username should be valid!");
        }
        return this;
    }
}
