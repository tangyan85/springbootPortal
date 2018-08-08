package com.wanda.portal.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanda.portal.constants.Constants;
import com.wanda.portal.constants.InputActionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_confluence_space")
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

    @Transient
    private Integer allPages;

    @Transient
    private String allPagesLink;

    @Transient
    private Integer createPages;

    @Transient
    private String modifyPagesLink;

    @Transient
    private Integer modifyPages;

    @Transient
    private String createPagesLink;

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

    public Integer getAllPages() {
        return allPages;
    }

    public void setAllPages(Integer allPages) {
        this.allPages = allPages;
    }

    public String getAllPagesLink() {
        return allPagesLink;
    }

    public void setAllPagesLink(String allPagesLink) {
        this.allPagesLink = allPagesLink;
    }

    public Integer getCreatePages() {
        return createPages;
    }

    public void setCreatePages(Integer createPages) {
        this.createPages = createPages;
    }

    public String getCreatePagesLink() {
        return createPagesLink;
    }

    public void setCreatePagesLink(String createPagesLink) {
        this.createPagesLink = createPagesLink;
    }

    public Integer getModifyPages() {
        return modifyPages;
    }

    public void setModifyPages(Integer modifyPages) {
        this.modifyPages = modifyPages;
    }

    public String getModifyPagesLink() {
        return modifyPagesLink;
    }

    public void setModifyPagesLink(String modifyPagesLink) {
        this.modifyPagesLink = modifyPagesLink;
    }

    @Override
    public String toString() {
        return "ConfluenceSpace{" +
                "spaceId=" + spaceId +
                ", spaceKey='" + spaceKey + '\'' +
                ", spaceName='" + spaceName + '\'' +
                ", spaceDescription='" + spaceDescription + '\'' +
                ", pageId='" + pageId + '\'' +
                ", serverIP='" + serverIP + '\'' +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", inputActionType=" + inputActionType +
                ", project=" + project +
                ", server=" + server +
                ", url='" + url + '\'' +
                ", webui='" + webui + '\'' +
                ", allPages=" + allPages +
                ", allPagesLink='" + allPagesLink + '\'' +
                ", createPages=" + createPages +
                ", modifyPagesLink='" + modifyPagesLink + '\'' +
                ", modifyPages=" + modifyPages +
                ", createPagesLink='" + createPagesLink + '\'' +
                '}';
    }
}
