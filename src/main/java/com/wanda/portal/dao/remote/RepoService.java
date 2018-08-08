package com.wanda.portal.dao.remote;

import com.alibaba.fastjson.JSONArray;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.dto.git.GitRepoDTO;
import com.wanda.portal.dto.svn.SubversionRepoDTO;
import com.wanda.portal.dto.svn.SvnTemplateWrapperDTO;
import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;

import java.util.List;

public interface RepoService {
    SvnTemplateWrapperDTO findSubversionTemplates(Server server);

    boolean checkIfSvnRepoExists(String repoName, Server server) throws Exception;

    SubversionRepoDTO findSvnRepo(String repoName, Server server) throws Exception;

    GitRepoDTO findGitRepo(String repoName, Server server) throws Exception;

    SubversionRepoDTO createSvnRepo(String repoName, Long applyTemplateId, boolean applyStandardLayout, Server server) throws Exception;
    
    List<SubversionRepoDTO> fetchAllSvnRepos(Server server);

    List<GitRepoDTO> fetchAllGitRepos(Server server);

    List<ScmRepoInputParam> fetchUnusedSvnRepos(Server server);

    List<ScmRepoInputParam> fetchUnusedGitRepos(Server server);

	List<ScmRepoInputParam> fetchAllTemplates(Server server);
	
	List<SCMRepo> findSvnByProjectId(Long projectId);
	
    void deleteByRepoId(Long repoId);

    List<ScmRepoInputParam> fetchScmByRepoType(Server server, RepoType repoType);

    GitRepoDTO createGitRepo(String repoName, Server server) throws Exception;

    JSONArray fetchAllBranches(Server server, Long id);

    List<ScmRepoInputParam> fetchAllScmByRepoType(Server server, RepoType repoType);
}
