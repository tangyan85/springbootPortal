package com.wanda.portal.facade.model;

import java.io.Serializable;
import java.util.Date;

public class ProjectModel implements Serializable{
	private static final long serialVersionUID = 1L;
    private Long projectId;
    private String projectKey;
    private String projectName;
    private String description;
    private String status;
    private Integer rank;
    private String remark;
    private Date createTime;
    
    private String jiraProjectLists;
    private String confluenceSpaceLists;
    private String jenkinsProjectLists;
    private String sCMRepoLists;
    private String artifactsLists;
    private String projectMemberLists;
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
		return createTime==null?new Date():new Date(createTime.getTime());
	}
	public void setCreateTime(Date createTime) {
		this.createTime = new Date(createTime.getTime());
	}
	public String getJiraProjectLists() {
		return jiraProjectLists;
	}
	public void setJiraProjectLists(String jiraProjectLists) {
		this.jiraProjectLists = jiraProjectLists;
	}
	public String getConfluenceSpaceLists() {
		return confluenceSpaceLists;
	}
	public void setConfluenceSpaceLists(String confluenceSpaceLists) {
		this.confluenceSpaceLists = confluenceSpaceLists;
	}
	public String getJenkinsProjectLists() {
		return jenkinsProjectLists;
	}
	public void setJenkinsProjectLists(String jenkinsProjectLists) {
		this.jenkinsProjectLists = jenkinsProjectLists;
	}
	public String getsCMRepoLists() {
		return sCMRepoLists;
	}
	public void setsCMRepoLists(String sCMRepoLists) {
		this.sCMRepoLists = sCMRepoLists;
	}
	public String getArtifactsLists() {
		return artifactsLists;
	}
	public void setArtifactsLists(String artifactsLists) {
		this.artifactsLists = artifactsLists;
	}
	public String getProjectMemberLists() {
		return projectMemberLists;
	}
	public void setProjectMemberLists(String projectMemberLists) {
		this.projectMemberLists = projectMemberLists;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "ProjectModel [projectId=" + projectId + ", projectKey="
				+ projectKey + ", projectName=" + projectName
				+ ", description=" + description + ", status=" + status
				+ ", rank=" + rank + ", remark=" + remark + ", createTime="
				+ createTime + ", jiraProjectLists=" + jiraProjectLists
				+ ", confluenceSpaceLists=" + confluenceSpaceLists
				+ ", jenkinsProjectLists=" + jenkinsProjectLists
				+ ", sCMRepoLists=" + sCMRepoLists + ", artifactsLists="
				+ artifactsLists + ", projectMemberLists=" + projectMemberLists
				+ "]";
	}

}
