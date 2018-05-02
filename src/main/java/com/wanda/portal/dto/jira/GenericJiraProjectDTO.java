package com.wanda.portal.dto.jira;

import java.io.Serializable;
import java.util.Map;

import com.wanda.portal.entity.JiraProject;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
import com.wanda.portal.utils.ConversionUtil;

/*
 * { 
 * "expand": "description,lead,url,projectKeys", 
 * "self": "http://10.214.170.115:8080/rest/api/2/project/10005", 
 * "id": "10005", 
 * "key": "ADLM", 
 * "name": "应用研发全生命周期管理平台", 
 * "avatarUrls": { 
 * "48x48": "http://10.214.170.115:8080/secure/projectavatar?avatarId=10324", 
 * "24x24": "http://10.214.170.115:8080/secure/projectavatar?size=small&avatarId=10324", 
 * "16x16": "http://10.214.170.115:8080/secure/projectavatar?size=xsmall&avatarId=10324", 
 * "32x32": "http://10.214.170.115:8080/secure/projectavatar?size=medium&avatarId=10324" }, 
 * "projectTypeKey":
 * "software" },
 */
/* 通用JIRA项目 */
public class GenericJiraProjectDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /* 扩展名 */
    private String expand;
    /* 项目自身链接 */
    private String self;
    /* 内部Id */
    private Long id;
    /* 唯一主键 */
    private String key;
    /* 项目名称，可以是中文 */
    private String name;
    /* software或者business */
    private String projectTypeKey;
    /* 图标，无大用 */
    private Map<String, String> avatarUrls;

    /* =====Getters and Setters below===== */
    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public Map<String, String> getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(Map<String, String> avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    @Override
    public String toString() {
        return "GenericJiraProjectDTO [expand=" + expand + ", self=" + self + ", id=" + id + ", key=" + key + ", name="
                + name + ", projectTypeKey=" + projectTypeKey + ", avatarUrls=" + avatarUrls + "]";
    }
    
    public JiraProjectInputParam getJiraProject(String serverIP){
    	return ConversionUtil.Con2JiraProject(this,serverIP);
    }
    public JiraProjectInputParam getJiraProject(){
    	return ConversionUtil.Con2JiraProject(this);
    }
}


