package com.wanda.portal.facade.model.input;

import java.io.Serializable;
import java.util.Date;

public class JenkinsInputParam extends AbstractInputParam implements Serializable {

    private static final long serialVersionUID = 6935957867878521579L;
    private Long jenkinsProjectId;

    private String jenkinsProjKey;
    
    private String referProj;

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
    
    public String getReferProj() {
        return referProj;
    }

    public void setReferProj(String referProj) {
        this.referProj = referProj;
    }

    public Long getJenkinsProjectId() {
        return jenkinsProjectId;
    }

    public void setJenkinsProjectId(Long jenkinsProjectId) {
        this.jenkinsProjectId = jenkinsProjectId;
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

    public Date getCreateTime() {
        return this.createTime == null ? new Date() : new Date(this.createTime.getTime());
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

    @Override
    public InputParam validateCreate() throws Exception {
//        if (StringUtils.isNullOrEmpty(this.jenkinsProjKey) || StringUtils.isNullOrEmpty(this.referProj)) {
//            throw new CreateProjectValidationException("jenkinsProjKey or referProj null");
//        }
        return this;
    }

    @Override
    public InputParam validateModify() throws Exception {
        return this;
    }
}
