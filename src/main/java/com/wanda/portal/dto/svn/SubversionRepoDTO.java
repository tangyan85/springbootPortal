package com.wanda.portal.dto.svn;

import java.io.Serializable;

import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import com.wanda.portal.utils.ConversionUtil;
/*
 *"id": 3,
  "name": "android_app",
  "status": "OK",
  "svnUrl": "https://10.214.170.65/svn/android_app",
  "viewvcUrl": "https://10.214.170.65/viewvc/android_app/"
 * */
public class SubversionRepoDTO implements Serializable {

	private static final long serialVersionUID = 8126215204917111115L;

	private Long id;
	private String name;
	private String svnUrl;
	private String viewvcUrl;
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public String getSvnUrl() {
		return svnUrl;
	}
	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}
	public String getViewvcUrl() {
		return viewvcUrl;
	}
	public void setViewvcUrl(String viewvcUrl) {
		this.viewvcUrl = viewvcUrl;
	}
	@Override
	public String toString() {
		return "SubversionRepoDTO [id=" + id + ", name=" + name + ", svnUrl="
				+ svnUrl + ", viewvcUrl=" + viewvcUrl + "]";
	}
	
	public ScmRepoInputParam getSCMRepo(String serverIP){
    	return ConversionUtil.Con2SCMRepo(this,serverIP);
    }
	public ScmRepoInputParam getSCMRepo(){
    	return ConversionUtil.Con2SCMRepo(this);
    }
}
