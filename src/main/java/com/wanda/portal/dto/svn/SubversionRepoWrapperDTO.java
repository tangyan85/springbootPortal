package com.wanda.portal.dto.svn;

import java.io.Serializable;
import java.util.List;

/*
 * 轮询subversion获得的repo列表
 * */
public class SubversionRepoWrapperDTO implements Serializable {

	private static final long serialVersionUID = 8543804221918418417L;

	private List<SubversionRepoDTO> repositories;

	public List<SubversionRepoDTO> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<SubversionRepoDTO> repositories) {
		this.repositories = repositories;
	}

	@Override
	public String toString() {
		return "SvnReposDTO [repositories=" + repositories + "]";
	}
	
}
