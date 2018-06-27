package com.wanda.portal.dao.remote;

import com.wanda.portal.constants.RepoType;
import com.wanda.portal.dto.git.GitRepoDTO;
import com.wanda.portal.dto.svn.SubversionRepoDTO;
import com.wanda.portal.dto.svn.SvnTemplateWrapperDTO;
import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;

import java.util.List;

public interface RepoService {
    SvnTemplateWrapperDTO findSubversionTemplates();

    boolean checkIfSvnRepoExists(String repoName) throws Exception;

    SubversionRepoDTO findSvnRepo(String repoName) throws Exception;

    GitRepoDTO findGitRepo(String repoName) throws Exception;

    SubversionRepoDTO createSvnRepo(String repoName, Long applyTemplateId, boolean applyStandardLayout) throws Exception;
    
    List<SubversionRepoDTO> fetchAllSvnRepos();

    List<GitRepoDTO> fetchAllGitRepos();

    List<ScmRepoInputParam> fetchUnusedSvnRepos();

    List<ScmRepoInputParam> fetchUnusedGitRepos();

	List<ScmRepoInputParam> fetchAllTemplates();
	
	List<SCMRepo> findSvnByProjectId(Long projectId);
	
	public void setServer(Server server);
	
	public Server getServer();

    void deleteByRepoId(Long repoId);

    List<ScmRepoInputParam> fetchScmByRepoType(Server server, RepoType repoType);

    GitRepoDTO createGitRepo(String repoName) throws Exception;
}
