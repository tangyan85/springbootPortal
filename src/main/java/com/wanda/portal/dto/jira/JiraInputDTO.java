package com.wanda.portal.dto.jira;

import java.io.Serializable;
import org.springframework.lang.Nullable;

/*
 * "key": "EX", // 唯一标识符 "name": "Example", // 项目名称 "projectTypeKey": "business", // 项目类型，自带6中
 * "projectTemplateKey": "com.atlassian.jira-core-project-templates:jira-core-project-management",
 * //? "description": "Example Project description", // 项目描述 "lead": "Charlie", // 项目leader "url":
 * "http://atlassian.com", // URL,可以拼装 "assigneeType": "PROJECT_LEAD", //？项目分配人角色 "avatarId": 10200,
 * // 可忽略 "issueSecurityScheme": 10001, // ?安全策略 "permissionScheme": 10011, // ?权限策略
 * "notificationScheme": 10021, // ？通知策略 "categoryId": 10120 //? categoryId 似乎是项目类别，可以查询得到
 */
public class JiraInputDTO implements Serializable {

    private static final long serialVersionUID = -570285032389918222L;

    private String key; // 唯一标识符

    private String name; // 项目名称

    private String projectTypeKey; // 项目类型，自带

    private String projectTemplateKey; // "com.atlassian.jira-core-project-templates:jira-core-project-management"

    private String description; // 项目描述

    private String lead; // 项目leader

    private String assigneeType; // 项目分配人角色
    
    @Nullable
    private String url; // url
    @Nullable
    private Long avatarId; // 可忽略
    @Nullable
    private Long issueSecurityScheme;
    @Nullable
    private Long permissionScheme;
    @Nullable
    private Long notificationScheme;
    @Nullable
    private Long categoryId;

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

    public String getProjectTemplateKey() {
        return projectTemplateKey;
    }

    public void setProjectTemplateKey(String projectTemplateKey) {
        this.projectTemplateKey = projectTemplateKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getAssigneeType() {
        return assigneeType;
    }

    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public Long getIssueSecurityScheme() {
        return issueSecurityScheme;
    }

    public void setIssueSecurityScheme(Long issueSecurityScheme) {
        this.issueSecurityScheme = issueSecurityScheme;
    }

    public Long getPermissionScheme() {
        return permissionScheme;
    }

    public void setPermissionScheme(Long permissionScheme) {
        this.permissionScheme = permissionScheme;
    }

    public Long getNotificationScheme() {
        return notificationScheme;
    }

    public void setNotificationScheme(Long notificationScheme) {
        this.notificationScheme = notificationScheme;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "JiraInputDTO [key=" + key + ", name=" + name + ", projectTypeKey=" + projectTypeKey
                + ", projectTemplateKey=" + projectTemplateKey + ", description=" + description + ", lead=" + lead
                + ", url=" + url + ", assigneeType=" + assigneeType + ", avatarId=" + avatarId
                + ", issueSecurityScheme=" + issueSecurityScheme + ", permissionScheme=" + permissionScheme
                + ", notificationScheme=" + notificationScheme + ", categoryId=" + categoryId + "]";
    }

}
