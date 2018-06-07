package com.wanda.portal.facade.model.input;

import com.mysql.jdbc.StringUtils;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.exception.CreateProjectValidationException;

import java.io.Serializable;
import java.util.Date;

public class ScmRepoInputParam extends AbstractInputParam implements Serializable {

    private static final long serialVersionUID = 1410327292154923094L;
    private Long repoId;

    private String repoName;
    
    private Long templateId;

    private Long repoRemoteId;

    private String serverIP;


    private Date createTime;

    private String remark;

    private RepoType repoType;

    private String repoStyle = "default";
    
    private Long serverId;

    private String webui;

    private String checkout;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }
    
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
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

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public Long getRepoRemoteId() {
        return repoRemoteId;
    }

    public void setRepoRemoteId(Long repoRemoteId) {
        this.repoRemoteId = repoRemoteId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RepoType getRepoType() {
        return repoType;
    }

    public void setRepoType(RepoType repoType) {
        this.repoType = repoType;
    }

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

    @Override
    public InputParam validateCreate() throws Exception {
        if (StringUtils.isNullOrEmpty(this.repoName)) {
            throw new CreateProjectValidationException("illegal repo name");
        }
        return this;
    }

    @Override
    public InputParam validateModify() throws Exception {
        return this;
    }   
    
}
