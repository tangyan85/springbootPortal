package com.wanda.portal.config.biz;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="myprops")
public class IdConfig {

	private String whoami;

    public String getWhoami() {
        return whoami;
    }

    public void setWhoami(String whoami) {
        this.whoami = whoami;
    }

}
