package com.wanda.portal.facade.model.input;

import java.io.Serializable;
import java.util.Date;
import com.mysql.jdbc.StringUtils;
import com.wanda.portal.exception.CreateProjectValidationException;

public class JiraProjectInputParam extends AbstractInputParam implements Serializable {

    private static final long serialVersionUID = 7863908341200132327L;

    private Long jiraProjectId;

    private String jiraProjectKey; // key

    private String jiraProjectName; // name

    private String jiraProjectDescription; // description

    private Date createTime;

    private String remark;
    
    private Long serverId; // 服务器Id

    private Long referJiraId; // 参考id

    private String teamleader; // teamleader
   
    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getTeamleader() {
        return teamleader;
    }

    public void setTeamleader(String teamleader) {
        this.teamleader = teamleader;
    }

    public Long getReferJiraId() {
        return referJiraId;
    }

    public void setReferJiraId(Long referJiraId) {
        this.referJiraId = referJiraId;
    }

    public Long getJiraProjectId() {
        return jiraProjectId;
    }

    public void setJiraProjectId(Long jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
    }

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }

    public String getJiraProjectName() {
        return jiraProjectName;
    }

    public void setJiraProjectName(String jiraProjectName) {
        this.jiraProjectName = jiraProjectName;
    }

    public String getJiraProjectDescription() {
        return jiraProjectDescription;
    }

    public void setJiraProjectDescription(String jiraProjectDescription) {
        this.jiraProjectDescription = jiraProjectDescription;
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

    @Override
    public InputParam validateCreate() throws Exception {
        /*
         * teamleader必须提供
         * jiraKey必须提供
         * jiraprojectName项目名称必须提供
         * referId参考项目Id必须提供（因为不用内置项目模板）
         * */
        if (StringUtils.isNullOrEmpty(this.teamleader) || StringUtils.isNullOrEmpty(this.jiraProjectKey)
                || StringUtils.isNullOrEmpty(this.jiraProjectName) || this.referJiraId == null) {
            throw new CreateProjectValidationException("Jira input param invalid");
        }        
        return this;
    }

    @Override
    public InputParam validateModify() throws Exception {
        /*
         * teamleader必须提供
         * jiraKey必须提供
         * jiraprojectName项目名称必须提供
         * */
        if (StringUtils.isNullOrEmpty(this.teamleader) || StringUtils.isNullOrEmpty(this.jiraProjectKey)
                || StringUtils.isNullOrEmpty(this.jiraProjectName)) {
            throw new CreateProjectValidationException("Edit Jira input param invalid");
        }        
        return this;
    }
}
