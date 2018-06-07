package com.wanda.portal.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanda.portal.constants.Constants;
import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.RepoType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "scm_repository")
public class SCMRepo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "repo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repoId;

    @Column(name = "repo_name")
    private String repoName;

    @Column(name = "server_ip")
    private String serverIP;
    
    @Column(name = "input_action_type")
    private InputActionType inputActionType;

    @Column(name = "create_time")
    @JSONField(format = Constants.YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @Column(name = "remark")
    private String remark;

    @Column(name = "repo_type")
    @Enumerated(EnumType.STRING)
    private RepoType repoType;

    @Column(name = "repo_style")
    private String repoStyle;

    @Column(name = "web_ui")
    private String webui;

    @Column(name = "check_out")
    private String checkout;

    
    private Long templateId;

    private Long repoRemoteId;

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

    @Transient
    private String urlForBrowsing;

    public SCMRepo() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((repoName == null) ? 0 : repoName.hashCode());
        result = prime * result + ((repoStyle == null) ? 0 : repoStyle.hashCode());
        result = prime * result + ((repoType == null) ? 0 : repoType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SCMRepo other = (SCMRepo) obj;
        if (repoName == null) {
            if (other.repoName != null)
                return false;
        } else if (!repoName.equals(other.repoName))
            return false;
        if (repoStyle == null) {
            if (other.repoStyle != null)
                return false;
        } else if (!repoStyle.equals(other.repoStyle))
            return false;
        if (repoType != other.repoType)
            return false;
        return true;
    }   

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public RepoType getRepoType() {
        return repoType;
    }

    public void setRepoType(RepoType repoType) {
        this.repoType = repoType;
    }

    public Long getRepoId() {
        return repoId;
    }

    public void setRepoId(Long repoId) {
        this.repoId = repoId;
    }

    public String getRepoName() {
        return repoName;
    }

    public InputActionType getInputActionType() {
		return inputActionType;
	}

	public void setInputActionType(InputActionType inputActionType) {
		this.inputActionType = inputActionType;
	}

	public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getRepoRemoteId() {
		return repoRemoteId;
	}

	public void setRepoRemoteId(Long repoRemoteId) {
		this.repoRemoteId = repoRemoteId;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlForBrowsing() {
        return urlForBrowsing;
    }

    public void setUrlForBrowsing(String urlForBrowsing) {
        this.urlForBrowsing = urlForBrowsing;
    }

//    public void generateUrl() {
//        this.url = protocol.toStr() + serverIP + serverRoot + repoName;
//    }
//
//    public void generateUrlForBrowsing() {
//        this.urlForBrowsing = Constants.REPO_BROWSING_PROTOCOL
//                + browsingServerIP + browsingServerRoot + repoName;
//    }

    public String getRepoStyle() {
        return repoStyle;
    }

    public void setRepoStyle(String repoStyle) {
        this.repoStyle = repoStyle;
    }

    public String getWebui() {
        return webui;
    }

    public void setWebui(String webui) {
        this.webui = webui;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }
}
