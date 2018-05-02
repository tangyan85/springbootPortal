package com.wanda.portal.config.biz;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myprops.confluence")
//@PropertySource("classpath:confluence.properties")
public class ConfluenceConfig {
	private String username;
	private String password;
	private String urlHead;
	private String genericApi;	
	private String pureIp;	
	
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
