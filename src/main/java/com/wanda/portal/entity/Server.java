package com.wanda.portal.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.wanda.portal.constants.ServerType;

@Entity
@Table(name = "server")
public class Server implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "server_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serverId;
    
    @Column(name = "server_type")
    @Enumerated(EnumType.STRING)
    private ServerType serverType;
    
    @Column(name = "inner_server_ip", length = 40)
    private String innerServerIpAndPort;
    
    @Column(name = "outer_server_ip", length = 40)
    private String outerServerIpAndPort;
    
    @Column(name = "remark", length = 255)
    private String remark;

    @Column(name = "protocol", length = 20)
    private String protocol;
   
    @Column(name = "reserved", length = 255)
    private String reserved;
    
    @Column(name = "server_root", length = 40)
    private String serverRoot;
    
    @Column(name = "login_suffix", length = 100)
    private String loginSuffix;

    @Column(name = "domain", length = 100)
    private String domain;
    
       
    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

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

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Server [serverId=" + serverId + ", serverType=" + serverType + ", innerServerIpAndPort="
                + innerServerIpAndPort + ", outerServerIpAndPort=" + outerServerIpAndPort + ", remark=" + remark + "]";
    }

	public String getLoginSuffix() {
		return loginSuffix;
	}

	public void setLoginSuffix(String loginSuffix) {
		this.loginSuffix = loginSuffix;
	}

	public String getServerRoot() {
		return serverRoot;
	}

	public void setServerRoot(String serverRoot) {
		this.serverRoot = serverRoot;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}  
    
}
