package com.wanda.portal.facade.model.input;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.wanda.portal.entity.Project;
import com.wanda.portal.exception.ModifyProjectValidationException;
import com.wanda.portal.utils.ValidationUtils;

public class ProjectInputParam extends AbstractInputParam implements Serializable {

    private static final long serialVersionUID = 7051382037255100192L;

    private Long projectId;
    private String projectKey;
    private String projectName;
    private String description;
    private String status;
    private Integer rank;
    private String remark;
    private Date createTime;

    private List<JiraProjectInputParam> jiraProjects;
    private List<ConfluenceSpaceInputParam> confluenceSpaces;
    private List<JenkinsInputParam> jenkinsProjects;
    private List<ScmRepoInputParam> scmRepositories;
    private List<ArtifactInputParam> artifacts;
    private List<ProjectMemberInputParam> projectMembers;

    public Project prepareToPesist() {
        Project proj = new Project();
        // BeanUtils.copyProperties(this, proj);
        return proj;
    }

    /*
     * 规范和校验创建project入参
     */
    @Override
    public InputParam validateCreate() throws Exception {
        this.projectId = null;
        ValidationUtils.validateDate(this.createTime);
        validByCondition(true);
        return this;
    }

    /*
     * 规范和校验修改project入参
     */
    @Override
    public InputParam validateModify() throws Exception {
        if (this.projectId == null) {
            throw new ModifyProjectValidationException("project id is null");
        }
        ValidationUtils.validateDate(this.createTime);
        validByCondition(false);
        return this;
    }

    @Override
    public void validByCondition(boolean isCreate) throws Exception {
        if (jiraProjects != null) {
            for (JiraProjectInputParam jira : jiraProjects) {
                jira.validByCondition(isCreate);
            }
        }
        if (confluenceSpaces != null) {
            for (ConfluenceSpaceInputParam conf : confluenceSpaces) {
                conf.validByCondition(isCreate);
            }
        }
        if (jenkinsProjects != null) {
            for (JenkinsInputParam jenk : jenkinsProjects) {
                jenk.validByCondition(isCreate);
            }
        }
        if (scmRepositories != null) {
            for (ScmRepoInputParam scm : scmRepositories) {
                scm.validByCondition(isCreate);
            }
        }
        if (artifacts != null) {
            for (ArtifactInputParam art : artifacts) {
                art.validByCondition(isCreate);
            }
        }
        if (projectMembers != null) {
            for (ProjectMemberInputParam pm : projectMembers) {
                pm.validByCondition(isCreate);
            }
        }
    }

    /*
     * =====================================GETTERs and SETTERs below===============================================
     */
    
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<JiraProjectInputParam> getJiraProjects() {
        return jiraProjects;
    }

    public void setJiraProjects(List<JiraProjectInputParam> jiraProjects) {
        this.jiraProjects = jiraProjects;
    }

    public List<ConfluenceSpaceInputParam> getConfluenceSpaces() {
        return confluenceSpaces;
    }

    public void setConfluenceSpaces(List<ConfluenceSpaceInputParam> confluenceSpaces) {
        this.confluenceSpaces = confluenceSpaces;
    }

    public List<JenkinsInputParam> getJenkinsProjects() {
        return jenkinsProjects;
    }

    public void setJenkinsProjects(List<JenkinsInputParam> jenkinsProjects) {
        this.jenkinsProjects = jenkinsProjects;
    }

    public List<ScmRepoInputParam> getScmRepositories() {
        return scmRepositories;
    }

    public void setScmRepositories(List<ScmRepoInputParam> scmRepositories) {
        this.scmRepositories = scmRepositories;
    }

    public List<ArtifactInputParam> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<ArtifactInputParam> artifacts) {
        this.artifacts = artifacts;
    }

    public List<ProjectMemberInputParam> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<ProjectMemberInputParam> projectMembers) {
        this.projectMembers = projectMembers;
    }

    @Override
    public String toString() {
        return "ProjectInputParam [projectId=" + projectId + ", projectKey=" + projectKey + ", projectName="
                + projectName + ", description=" + description + ", status=" + status + ", rank=" + rank + ", remark="
                + remark + ", createTime=" + createTime + ", jiraProjects=" + jiraProjects + ", confluenceSpaces="
                + confluenceSpaces + ", jenkinsProjects=" + jenkinsProjects + ", scmRepositories=" + scmRepositories
                + ", artifacts=" + artifacts + ", projectMembers=" + projectMembers + "]";
    }

}
