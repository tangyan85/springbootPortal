package com.wanda.portal.dto.jira;

import java.io.Serializable;

public class JiraProjectComponentDTO implements Serializable {
    private Long id;
    private Integer issues;
    private String name;
    private String description;
    private String displayName;
    private String self;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getIssues() {
        return issues;
    }

    public void setIssues(Integer issues) {
        this.issues = issues;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    @Override
    public String toString() {
        return "JiraProjectComponentDTO{" +
                "id=" + id +
                ", issues=" + issues +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                ", self='" + self + '\'' +
                '}';
    }
}
