package com.wanda.portal.dao.remote;

import java.util.List;

import com.wanda.portal.dto.svn.SubversionRepoDTO;
import com.wanda.portal.dto.svn.SvnTemplateWrapperDTO;
import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;

public interface RepoService {
    SvnTemplateWrapperDTO findSubversionTemplates();

    boolean checkIfSvnRepoExists(String repoName) throws Exception;

    SubversionRepoDTO createSvnRepo(String repoName, Long applyTemplateId, boolean applyStandardLayout) throws Exception;
    
    List<SubversionRepoDTO> fetchAllSvnRepos();
    
    List<ScmRepoInputParam> fetchUnusedSvnRepos();

	List<ScmRepoInputParam> fetchAllTemplates();
	
	List<SCMRepo> findSvnByProjectId(Long projectId);
	
	public void setServer(Server server);
	
	public Server getServer();
}
