package com.wanda.portal.facade.model.output;

import java.io.Serializable;

import com.wanda.portal.constants.ServerType;

public class ServerOutputParam implements Serializable {
    private Long serverId;
    private ServerType serverType;
    private String innerServerIpAndPort;
    private String outerServerIpAndPort;
    private String remark;
    private String protocol;
    private String reserved;
    private String serverRoot;
    private String loginSuffix;
    private String domain;
    
    private String loginUrl;
    private String urlPrefix;
    private String urlContext;
    
    
	public Long getServerId() {
		return serverId;
	}
	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
	public ServerType getServerType() {
		return serverType;
	}
	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}
	public String getInnerServerIpAndPort() {
		return innerServerIpAndPort;
	}
	public void setInnerServerIpAndPort(String innerServerIpAndPort) {
		this.innerServerIpAndPort = innerServerIpAndPort;
	}
	public String getOuterServerIpAndPort() {
		return outerServerIpAndPort;
	}
	public void setOuterServerIpAndPort(String outerServerIpAndPort) {
		this.outerServerIpAndPort = outerServerIpAndPort;
	}
	public String getRemark() {
		return remark;
	}
	public String getServerRoot() {
		return serverRoot;
	}
	public void setServerRoot(String serverRoot) {
		this.serverRoot = serverRoot;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public String getLoginSuffix() {
		return loginSuffix;
	}

	public void setLoginSuffix(String loginSuffix) {
		this.loginSuffix = loginSuffix;
	}
	public String getUrlPrefix() {
		return urlPrefix;
	}
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getUrlContext() {
		return urlContext;
	}
	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
	}
}
