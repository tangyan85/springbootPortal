package com.wanda.portal.entity;

import com.wanda.portal.constants.ServerType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_server")
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

    @Column(name = "login_name", length = 100)
    private String loginName;

    @Column(name = "passwd", length = 100)
    private String passwd;

    @Column(name = "login_mode", length = 1)
    private String loginMode;

    @Transient
    private boolean authAdmin;

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
        return "Server{" +
                "serverId=" + serverId +
                ", serverType=" + serverType +
                ", innerServerIpAndPort='" + innerServerIpAndPort + '\'' +
                ", outerServerIpAndPort='" + outerServerIpAndPort + '\'' +
                ", remark='" + remark + '\'' +
                ", protocol='" + protocol + '\'' +
                ", reserved='" + reserved + '\'' +
                ", serverRoot='" + serverRoot + '\'' +
                ", loginSuffix='" + loginSuffix + '\'' +
                ", domain='" + domain + '\'' +
                ", loginName='" + loginName + '\'' +
                ", passwd='" + passwd + '\'' +
                ", loginMode='" + loginMode + '\'' +
                '}';
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    public boolean isAuthAdmin() {
        return authAdmin;
    }

    public void setAuthAdmin(boolean authAdmin) {
        this.authAdmin = authAdmin;
    }
}
