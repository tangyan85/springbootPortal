package com.wanda.portal.dto.git;

import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import com.wanda.portal.utils.ConversionUtil;

public class GitRepoDTO {
    private Long id;
    private String name;
    private String http_url_to_repo;
    private String web_url;
    private String visibility;
    private String name_with_namespace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getHttp_url_to_repo() {
        return http_url_to_repo;
    }

    public void setHttp_url_to_repo(String http_url_to_repo) {
        this.http_url_to_repo = http_url_to_repo;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getName_with_namespace() {
        return name_with_namespace;
    }

    public void setName_with_namespace(String name_with_namespace) {
        this.name_with_namespace = name_with_namespace;
    }

    public ScmRepoInputParam getSCMRepo(){
        return ConversionUtil.Con2SCMRepo(this);
    }
}
