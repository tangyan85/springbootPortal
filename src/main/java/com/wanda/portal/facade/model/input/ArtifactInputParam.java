package com.wanda.portal.facade.model.input;

import java.io.Serializable;
import java.util.Date;
import com.wanda.portal.constants.AritifactType;
import com.wanda.portal.utils.ValidationUtils;

public class ArtifactInputParam extends AbstractInputParam implements Serializable{

    private static final long serialVersionUID = 8633840982979249006L;
    private Long artifactId;

    private AritifactType artifactType;
    
    private String rootPath;

    private String serverIP;

    private Date createTime;
    
    private Long serverId;  

    public AritifactType getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(AritifactType artifactType) {
        this.artifactType = artifactType;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(Long artifactId) {
        this.artifactId = artifactId;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public InputParam validateCreate() throws Exception {
        this.artifactId = null;
        ValidationUtils.validateDate(this.createTime);
        return this;
    }

    @Override
    public InputParam validateModify() throws Exception {
        // 空的id是否代表创建？
        ValidationUtils.validateDate(this.createTime);
        return this;
    }
    
}
