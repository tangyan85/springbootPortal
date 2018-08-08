package com.wanda.portal.dto.jira;

import java.io.Serializable;

/**
 * Jira项目版本信息
 *
 * @author cloud he
 * @since 1.0.0 2018/7/4
 */
public class JiraProjectVersionDTO implements Serializable {
    private String self;
    private Long id;
    private String description;
    private String name;
    private Boolean archived;
    private Boolean released;
    private String startDate;
    private String releaseDate;
    private Boolean overdue;
    private String userStartDate;
    private String userReleaseDate;
    private Long projectId;
    private String url;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getReleased() {
        return released;
    }

    public void setReleased(Boolean released) {
        this.released = released;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }

    public String getUserStartDate() {
        return userStartDate;
    }

    public void setUserStartDate(String userStartDate) {
        this.userStartDate = userStartDate;
    }

    public String getUserReleaseDate() {
        return userReleaseDate;
    }

    public void setUserReleaseDate(String userReleaseDate) {
        this.userReleaseDate = userReleaseDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "JiraProjectVersionDTO{" +
                "self='" + self + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", archived=" + archived +
                ", released=" + released +
                ", startDate='" + startDate + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", overdue=" + overdue +
                ", userStartDate='" + userStartDate + '\'' +
                ", userReleaseDate='" + userReleaseDate + '\'' +
                ", projectId=" + projectId +
                ", url='" + url + '\'' +
                '}';
    }
}
