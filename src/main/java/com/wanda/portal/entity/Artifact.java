package com.wanda.portal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanda.portal.constants.AritifactType;
import com.wanda.portal.constants.Constants;

@Entity
@Table(name = "nexus_artifacts")
public class Artifact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "artifact_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artifactId;

    @Column(name = "root_path")
    private String rootPath;

    @Column(name = "server_ip")
    private String serverIP;
    
    @Column(name = "create_time")
    @JSONField(format = Constants.YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @Column(name = "artifact_type")
    @Enumerated(EnumType.STRING)
    private AritifactType aritifactType;
  
    @ManyToOne(fetch = FetchType.LAZY) // 注意ManyToOne如果采用Eager会导致糟糕的性能，参见vladmihalcea的博客 
    @JoinColumn(name = "project_id") // 这里记得配置id，否则会默认生成难看的project_project_id
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getURLRoot() {
        if (AritifactType.MAVEN == aritifactType) {
            return rootPath.replace('.', '/');
        }
        return rootPath;
    }

    public void generateUrl() {
        this.url = Constants.NEXUS_BROWSING_PROTOCOL + serverIP + Constants.NEXUS_PORT + Constants.NEXUS_VIEWPROJ_PREFIX + aritifactType.getPrefix() + getURLRoot();
    }

    public AritifactType getAritifactType() {
        return aritifactType;
    }

    public void setAritifactType(AritifactType aritifactType) {
        this.aritifactType = aritifactType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public Date getCreateTime() {
		return createTime==null?new Date():new Date(createTime.getTime());
	}

	public void setCreateTime(Date createTime) {
		this.createTime = new Date(createTime.getTime());
	}
}
