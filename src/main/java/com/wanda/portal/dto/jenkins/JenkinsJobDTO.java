package com.wanda.portal.dto.jenkins;

import com.wanda.portal.facade.model.input.JenkinsInputParam;
import com.wanda.portal.utils.ConversionUtil;

import java.io.Serializable;

/*
 * { "_class" : "com.cloudbees.hudson.plugins.folder.Folder", "displayName" :
 * "ADLM - 应用研发全生命周期管理平台", "name" : "adlm" },
 */
public class JenkinsJobDTO implements Serializable {

    private static final long serialVersionUID = -8284943630350038890L;

    private String _class;

    private String displayName;

    private String name;

    private String url;

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "JenkinsJobDTO [_class=" + _class + ", displayName=" + displayName + ", name=" + name + "]";
    }

    public JenkinsInputParam getJenkinsProject(String serverIP){
    	return ConversionUtil.Con2JenkinsProject(this,serverIP);
    }
    public JenkinsInputParam getJenkinsProject(){
    	return ConversionUtil.Con2JenkinsProject(this);
    }
}
