package com.wanda.portal.config.biz;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myprops.jira")
//@PropertySource("classpath:jira.properties")
public class JiraConfig {
	private String username;
	private String password;
	private String urlHead;
	private String pureIp;
	private String genericApi;
	private String oldCreateApi;	
		
	public String getPureIp() {
        return pureIp;
    }
    public void setPureIp(String pureIp) {
        this.pureIp = pureIp;
    }
    public String getGenericApi() {
        return genericApi;
    }
    public void setGenericApi(String genericApi) {
        this.genericApi = genericApi;
    }
    public String getOldCreateApi() {
        return oldCreateApi;
    }
    public void setOldCreateApi(String oldCreateApi) {
        this.oldCreateApi = oldCreateApi;
    }
    public String getUrlHead() {
		return urlHead;
	}
	public void setUrlHead(String urlHead) {
		this.urlHead = urlHead;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
