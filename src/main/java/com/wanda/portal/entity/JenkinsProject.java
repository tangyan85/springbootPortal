package com.wanda.portal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanda.portal.constants.Constants;
import com.wanda.portal.constants.InputActionType;

@Entity
@Table(name = "jenkins_project")
public class JenkinsProject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "jenkins_proj_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jenkinsProjectId;

    @Column(name = "jenkins_proj_key", unique=true)
    private String jenkinsProjKey;

    @Column(name = "server_ip")
    private String serverIP;

    @Column(name = "create_time")
    @JSONField(format = Constants.YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @Column(name = "remark")
    private String remark;
    
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
   
    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
    
    @Transient
    private String url;

    public JenkinsProject() {
    }

    public String getJenkinsProjKey() {
        return jenkinsProjKey;
    }

    public void setJenkinsProjKey(String jenkinsProjKey) {
        this.jenkinsProjKey = jenkinsProjKey;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }


    public Long getJenkinsProjectId() {
        return jenkinsProjectId;
    }

    public void setJenkinsProjectId(Long jenkinsProjectId) {
        this.jenkinsProjectId = jenkinsProjectId;
    }

    public Date getCreateTime() {
    	return this.createTime==null?new Date():new Date(createTime.getTime());
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
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void generateURL() {
        this.url = Constants.JENKINS_BROWSING_PROTOCOL + serverIP + Constants.JENKINS_PORT + Constants.JENKINS_VIEWPROJ_PREFIX + jenkinsProjKey;
    }

	public InputActionType getInputActionType() {
		return inputActionType;
	}

	public void setInputActionType(InputActionType inputActionType) {
		this.inputActionType = inputActionType;
	}


}
