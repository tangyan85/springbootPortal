package com.wanda.portal.config.biz;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myprops.jenkins")
//@PropertySource("classpath:jenkins.properties")
public class JenkinsConfig {
	private String username;
	private String password;
	private String urlHead;
	private String pureIp;
	private String jsonQueryJobsApi;
	private String createItem;	
	
    public String getPureIp() {
        return pureIp;
    }
    public void setPureIp(String pureIp) {
        this.pureIp = pureIp;
    }
    public String getCreateItem() {
        return createItem;
    }
    public void setCreateItem(String createItem) {
        this.createItem = createItem;
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
    public String getUrlHead() {
        return urlHead;
    }
    public void setUrlHead(String urlHead) {
        this.urlHead = urlHead;
    }
    public String getJsonQueryJobsApi() {
        return jsonQueryJobsApi;
    }
    public void setJsonQueryJobsApi(String jsonQueryJobsApi) {
        this.jsonQueryJobsApi = jsonQueryJobsApi;
    }
	
}
