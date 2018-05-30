package com.wanda.portal.config.biz;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统功能开关属性配置
 * 
 * @since 1.0.0 2018/5/8
 * @author cloud He
 */
@Component
@ConfigurationProperties(prefix = "myprops.switch")
public class SwitchConfig {
	/**
	 * 是否使用单点登录
	 */
	private Boolean enableSso;

	public Boolean isEnableSso() {
		return enableSso;
	}

	public void setEnableSso(Boolean enableSso) {
		this.enableSso = enableSso;
	}
	
}
