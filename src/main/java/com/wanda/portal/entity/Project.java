package com.wanda.portal.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.alibaba.fastjson.annotation.JSONField;
import com.wanda.portal.constants.Constants;
import com.wanda.portal.constants.ProjectMemberRole;
import com.wanda.portal.constants.ProjectStatus;

/*
 * Portal的项目主类
 * */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "project_key", unique=true)
    private String projectKey;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "description")
    private String description;
  
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "remark")
    private String remark;

    @Column(name = "create_time")
    @JSONField(format = Constants.YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<JiraProject> jiraProjects = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ConfluenceSpace> confluenceSpaces = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<JenkinsProject> jenkinsProjects = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<SCMRepo> scmRepositories = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Artifact> artifacts = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProjectMember> projectMembers = new HashSet<>();

    public Project() {
    }
	
	/*------------------ADDER BEGIN------------------*/
	public void addProjectMember(ProjectMember projectMember) {
        projectMembers.add(projectMember);
        projectMember.setProject(this);
    }

    public void addAritifacts(Artifact artifact) {
        artifacts.add(artifact);
        artifact.setProject(this);
    }

    public void addJenkinsProject(JenkinsProject jenkinsProject) {
        jenkinsProjects.add(jenkinsProject);
        jenkinsProject.setProject(this);
    }

    public void addJiraProject(JiraProject jiraProject) {
        jiraProjects.add(jiraProject);
        jiraProject.setProject(this);
    }

    public void addScmRepo(SCMRepo scmRepo) {
        scmRepositories.add(scmRepo);
        scmRepo.setProject(this);
    }

    public void addConfluenceSpace(ConfluenceSpace confluenceSpace) {
        confluenceSpaces.add(confluenceSpace);
        confluenceSpace.setProject(this);
    }
    /*------------------ADDER OVER------------------*/
	/*------------------REMOVER BEGIN------------------*/
	public void removeProjectMember(ProjectMember projectMember) {
        projectMembers.remove(projectMember);
        projectMember.setProject(null);
    }

    public void removeAritifacts(Artifact artifact) {
        artifacts.remove(artifact);
        artifact.setProject(null);
    }

    public void removeJenkinsProject(JenkinsProject jenkinsProject) {
        jenkinsProjects.remove(jenkinsProject);
        jenkinsProject.setProject(null);
    }

    public void removeJiraProject(JiraProject jiraProject) {
        jiraProjects.remove(jiraProject);
        jiraProject.setProject(null);
    }

    public void removeScmRepo(SCMRepo scmRepo) {
        scmRepositories.remove(scmRepo);
        scmRepo.setProject(null);
    }

    public void removeConfluenceSpace(ConfluenceSpace confluenceSpace) {
        confluenceSpaces.remove(confluenceSpace);
        confluenceSpace.setProject(null);
    }
	/*------------------REMOVER OVER------------------*/
   

    public Long getProjectId() {
        return projectId;
    }

    public Set<JiraProject> getJiraProjects() {
        return jiraProjects;
    }

    public void setJiraProjects(Set<JiraProject> jiraProjects) {
        this.jiraProjects = jiraProjects;
    }

    public Set<ConfluenceSpace> getConfluenceSpaces() {
        return confluenceSpaces;
    }

    public void setConfluenceSpaces(Set<ConfluenceSpace> confluenceSpaces) {
        this.confluenceSpaces = confluenceSpaces;
    }

    public Set<JenkinsProject> getJenkinsProjects() {
        return jenkinsProjects;
    }

    public void setJenkinsProjects(Set<JenkinsProject> jenkinsProjects) {
        this.jenkinsProjects = jenkinsProjects;
    }

    public Set<SCMRepo> getScmRepositories() {
        return scmRepositories;
    }

    public void setScmRepositories(Set<SCMRepo> scmRepositories) {
        this.scmRepositories = scmRepositories;
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public Set<ProjectMember> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<ProjectMember> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getCreateTime() {
    	return createTime==null?new Date():new Date(createTime.getTime());
    }

    public void setCreateTime(Date createTime) {
        this.createTime = new Date(createTime.getTime());
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }


    public List<String> getProjectManagers() {
        List<String> result = new ArrayList<>();
        if (null == getProjectMembers()) {
        	return result;
        }
        for (ProjectMember member : getProjectMembers()) {
            if (ProjectMemberRole.PM == member.getRole()) {
                result.add(member.getUsername());
            }
        }
        return result;
    }

    public List<String> getDevelopers() {
        List<String> result = new ArrayList<>();
        if (null == getProjectMembers()) {
        	return result;
        }
        for (ProjectMember member : getProjectMembers()) {
            if (ProjectMemberRole.DEV == member.getRole()) {
                result.add(member.getUsername());
            }
        }
        return result;
    }

    public List<String> getQAs() {
        List<String> result = new ArrayList<>();       
        if (null == getProjectMembers()) {
        	return result;
        }        
        for (ProjectMember member : getProjectMembers()) {
            if (ProjectMemberRole.QA == member.getRole()) {
                result.add(member.getUsername());
            }
        }
        return result;
    }
}
