package com.wanda.portal.dto.jira;

import java.io.Serializable;

/*
 * { "self": "http://10.214.170.115:8080/rest/api/2/project/10310", "id": 10310, "key": "PPP" }
 */
public class JiraOutputDTO implements Serializable {

    private static final long serialVersionUID = 975967855366755268L;

    private String self; // 项目地址

    private Long id; // 项目id

    private String key; // 项目Key

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "JiraOutputDTO [self=" + self + ", id=" + id + ", key=" + key + "]";
    }

}
