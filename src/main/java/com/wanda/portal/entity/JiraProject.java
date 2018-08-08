package com.wanda.portal.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanda.portal.constants.Constants;
import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.dto.jira.JiraProjectVersionDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_jira_project")
public class JiraProject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "jira_proj_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jiraProjectId;

    @Column(name = "jira_proj_key", unique = true)
    private String jiraProjectKey;

    @Column(name = "jira_proj_name")
    private String jiraProjectName;

    @Column(name = "jira_proj_description")
    private String jiraProjectDescription;

    @Column(name = "create_time")
    @JSONField(format = Constants.YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @Column(name = "remark")
    private String remark;

    @Column(name = "server_ip")
    private String serverIP;

    @Column(name = "team_leader")
    private String teamleader;

    @Column(name = "web_ui")
    private String webui;

    @Column(name = "refer_jiraId")
    private Long referJiraId; // 参考id

    @Column(name = "input_action_type")
    private InputActionType inputActionType;

    @ManyToOne(fetch = FetchType.LAZY) // 注意ManyToOne如果采用Eager会导致糟糕的性能，参见vladmihalcea的博客 
    @JoinColumn(name = "project_id") //这里记得配置id，否则会默认生成难看的project_project_id
    @JSONField(serialize=false)
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER) // 注意ManyToOne如果采用Eager会导致糟糕的性能，参见vladmihalcea的博客 
    @JoinColumn(name = "server_id") //这里记得配置id，否则会默认生成难看的project_project_id
    @JSONField(serialize=false)
    private Server server;

    @Transient
    private List<JiraProjectVersionDTO> projectVersions;

    @Transient
    private Integer allIssues;

    @Transient
    private Integer finishIssues;

	public Server getServer() {
        return server;
    }


    public void setServer(Server server) {
        this.server = server;
    }

    @Transient
    private String url;

    public JiraProject() {
    }

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }

    public String getJiraProjectDescription() {
        return jiraProjectDescription;
    }

    public void setJiraProjectDescription(String jiraProjectDescription) {
        this.jiraProjectDescription = jiraProjectDescription;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getJiraProjectId() {
        return jiraProjectId;
    }

    public void setJiraProjectId(Long jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
    }

    public String getJiraProjectName() {
        return jiraProjectName;
    }

    public void setJiraProjectName(String jiraProjectName) {
        this.jiraProjectName = jiraProjectName;
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

    public String getUrl() {

        return Constants.JIRA_BROWSING_PROTOCOL + server.getOuterServerIpAndPort() + Constants.JIRA_VIEWPROJ_PREFIX + jiraProjectKey;
    }

    public void setUrl(String url) {
        this.url = url;
    }


	public String getTeamleader() {
		return teamleader;
	}


	public void setTeamleader(String teamleader) {
		this.teamleader = teamleader;
	}


	public InputActionType getInputActionType() {
		return inputActionType;
	}


	public void setInputActionType(InputActionType inputActionType) {
		this.inputActionType = inputActionType;
	}


	public Long getReferJiraId() {
		return referJiraId;
	}


	public void setReferJiraId(Long referJiraId) {
		this.referJiraId = referJiraId;
	}


	public String getServerIP() {
		return serverIP;
	}


	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

//    public void generateUrl() {
//        this.url = Constants.JIRA_BROWSING_PROTOCOL + serverIP + Constants.JIRA_PORT + Constants.JIRA_VIEWPROJ_PREFIX + jiraProjectKey;
//    }

    public String getWebui() {
        return webui;
    }

    public void setWebui(String webui) {
        this.webui = webui;
    }

    public List<JiraProjectVersionDTO> getProjectVersions() {
        return projectVersions;
    }

    public void setProjectVersions(List<JiraProjectVersionDTO> projectVersions) {
        this.projectVersions = projectVersions;
    }

    public Integer getAllIssues() {
        return allIssues;
    }

    public void setAllIssues(Integer allIssues) {
        this.allIssues = allIssues;
    }

    public Integer getFinishIssues() {
        return finishIssues;
    }

    public void setFinishIssues(Integer finishIssues) {
        this.finishIssues = finishIssues;
    }
}
