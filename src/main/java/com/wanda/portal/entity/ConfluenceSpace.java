package com.wanda.portal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanda.portal.constants.Constants;
import com.wanda.portal.constants.InputActionType;

@Entity
@Table(name = "confluence_space")
public class ConfluenceSpace implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "space_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spaceId;

    @Column(name = "space_key", unique = true)
    private String spaceKey;

    @Column(name = "space_name")
    private String spaceName;

    @Column(name = "space_description")
    private String spaceDescription;

    @Column(name = "page_id")
    private String pageId;

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
    
    @Column(name = "web_ui")
    private String webui;

    public String getWebui() {
        return webui;
    }

    public void setWebui(String webui) {
        this.webui = webui;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceKey() {
        return spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getSpaceDescription() {
        return spaceDescription;
    }

    public void setSpaceDescription(String spaceDescription) {
        this.spaceDescription = spaceDescription;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
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

    public void generateUrl() {
        this.url = Constants.CONF_BROWSING_PROTOCOL + serverIP + Constants.CONF_PORT + Constants.CONF_VIEWPAGE_PREFIX + pageId;
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

	public InputActionType getInputActionType() {
		return inputActionType;
	}

	public void setInputActionType(InputActionType inputActionType) {
		this.inputActionType = inputActionType;
	}
}
