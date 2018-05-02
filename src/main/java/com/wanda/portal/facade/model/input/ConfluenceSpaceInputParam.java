package com.wanda.portal.facade.model.input;

import java.io.Serializable;
import java.util.Date;

public class ConfluenceSpaceInputParam extends AbstractInputParam implements Serializable {

    private static final long serialVersionUID = -2107952422387932491L;
    private Long spaceId;

    private String spaceKey;

    private String spaceName;

    private String spaceDescription;

    private String pageId;

    private String serverIP;

    private Date createTime;
    
    private String remark;
    
    private Long serverId;  

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
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

    public Date getCreateTime() {
        return this.createTime == null ? new Date() : new Date(this.createTime.getTime());
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public InputParam validateCreate() throws Exception {
        return this;
    }

    @Override
    public InputParam validateModify() throws Exception {
        return this;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
