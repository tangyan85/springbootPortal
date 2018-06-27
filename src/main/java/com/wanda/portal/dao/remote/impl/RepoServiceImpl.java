package com.wanda.portal.dao.remote.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.wanda.portal.config.biz.SvnConfig;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.dao.jpa.ProjectRepository;
import com.wanda.portal.dao.jpa.SCMRepoRepository;
import com.wanda.portal.dao.remote.RepoService;
import com.wanda.portal.dto.git.GitRepoDTO;
import com.wanda.portal.dto.svn.SubversionRepoDTO;
import com.wanda.portal.dto.svn.SubversionRepoWrapperDTO;
import com.wanda.portal.dto.svn.SvnTemplateDTO;
import com.wanda.portal.dto.svn.SvnTemplateWrapperDTO;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.entity.Server;
import com.wanda.portal.exception.SvnRepoCreateFailureException;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import com.wanda.portal.utils.ConversionUtil;
import com.wanda.portal.utils.RegexUtils;
import com.wanda.portal.utils.RestLogUtils;
import com.wanda.portal.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Primary
@Service
@Scope("prototype")
public class RepoServiceImpl implements RepoService {

    private static final String errorMessage = "errorMessage";
    private static final String message = "message";

    private static Logger LOGGER = LoggerFactory.getLogger(RepoServiceImpl.class);

    private Server server;

    @Autowired
    SvnConfig svnConfig;
    @Autowired
    RestTemplate restTemplate;
    private static final String REPO_PARAM_ILLEGAL_ERROR_MSG = "repoName cannot be null or empty!";
    private static final String REPO_CREATE_ERROR_MSG = "error occurred during createing repo";
    private static final String REPO_ALREADY_EXIST_MSG = "repo already exists!";
    private static final String REPO_CREATE_FAILED_MSG = "repo creation denied by subvesion";
    @Autowired
	ProjectRepository projectRepository;
    @Override
    public boolean checkIfSvnRepoExists(String repoName) throws Exception {
        LOGGER.info("===Begin query SVN for reponame-" + repoName);
        if (StringUtils.isNullOrEmpty(repoName) || !RegexUtils.isValidRepo(repoName)) { // 如果仓库名为空或者不合法，则直接拒绝
            throw new SvnRepoCreateFailureException(REPO_PARAM_ILLEGAL_ERROR_MSG);
        }

        List<SubversionRepoDTO> allRepos = fetchAllSvnRepos();
        for (SubversionRepoDTO rpo : allRepos) { // 遍历已有repo
            if (repoName.equals(rpo.getName())) { // 找到则返回存在
                LOGGER.info("repoName " + repoName + " exists!");
                LOGGER.info("===End query SVN for reponame" + repoName);
                return true;
            }
        }

        LOGGER.info("repoName " + repoName + " not exist");
        LOGGER.info("===End query SVN for reponame" + repoName);
        return false;
    }

    @Override
    public SubversionRepoDTO findSvnRepo(String repoName) throws Exception {
        LOGGER.info("===Begin query SVN for reponame-" + repoName);
        // 如果仓库名为空或者不合法，则直接拒绝
        if (StringUtils.isNullOrEmpty(repoName) || !RegexUtils.isValidRepo(repoName)) {
            throw new SvnRepoCreateFailureException(REPO_PARAM_ILLEGAL_ERROR_MSG);
        }

        List<SubversionRepoDTO> allRepos = fetchAllSvnRepos();
        // 遍历已有repo
        for (SubversionRepoDTO rpo : allRepos) {
            // 找到则返回存在
            if (repoName.equals(rpo.getName())) {
                return rpo;
            }
        }

        return null;
    }

    @Override
    public GitRepoDTO findGitRepo(String repoName) throws Exception {
        LOGGER.info("===Begin query SVN for reponame-" + repoName);
        // 如果仓库名为空或者不合法，则直接拒绝
        if (StringUtils.isNullOrEmpty(repoName) || !RegexUtils.isValidRepo(repoName)) {
            throw new SvnRepoCreateFailureException(REPO_PARAM_ILLEGAL_ERROR_MSG);
        }

        List<GitRepoDTO> allRepos = fetchAllGitRepos();
        // 遍历已有repo
        for (GitRepoDTO rpo : allRepos) {
            // 找到则返回存在
            if (repoName.equals(rpo.getName())) {
                return rpo;
            }
        }

        return null;
    }

    /**
     * 入参1 repoName 代表repo文件夹的名字，首先会根据正则做简单的名字校验，并且轮询subversion看一下此repo是否有效
     * 入参2 applyTemplateId 代表模板id，此id为轮询subversion获得的模板id，输入负数表示越过模板选择，而采用subversion默认
     * 入参3 applyStandardLayout 代表标准风格，这仅在模板Id为负的时候生效，true代表branch/tag/trunk风格，false代表裸 出参:
     * 创建途中失败抛出SvnRepoCreateFailureException异常，创建成功返回SubversionRepoDTO
     */
    @Override
    public final SubversionRepoDTO createSvnRepo(String repoName, Long applyTemplateId, boolean applyStandardLayout)
            throws Exception {
        LOGGER.info("===Begin create Subversion, name = " + repoName + ", applyTemplateId = " + applyTemplateId
                + ", applyStandardLayout = " + applyStandardLayout);
        // templateId和applyStandardLayout二者并非都必填，如果选择templateId
        // 则根据模板进行创建，否则根据applyStandardLayout=true/false，进行默认风格的建立
        boolean isExist = checkIfSvnRepoExists(repoName); // 存在吗？
        if (isExist) {
            LOGGER.warn("End create Subversion Abnormally==="); // 存在就不能创建了
            throw new SvnRepoCreateFailureException(REPO_ALREADY_EXIST_MSG);
        }
        HttpHeaders headers = RestUtils.packBasicAuthHeader(svnConfig.getUsername(), svnConfig.getPassword());
        // headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // List<MediaType> accps = new ArrayList<MediaType>();
        // accps.add(MediaType.APPLICATION_JSON_UTF8);
        // headers.setAccept(accps);
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        Map<String, Object> params = new HashMap<String, Object>(); // 这里存放Post请求的入参Map
        params.put("name", repoName);
        if (applyTemplateId > 1) { // 传入有效的TemplateId，方可指定模板，输入负数表示越过模板采用subversion默认 传1会有400错误
            params.put("applyTemplateId", applyTemplateId);
        }
        params.put("applyStandardLayout", applyStandardLayout); // true代表branch/tag/trunk风格，false代表裸
        String requestBody = JSONObject.toJSONString(params);
        HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, headers);

        try {
            response = restTemplate.exchange(server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                    + svnConfig.getGenericApi() + "repository?format=json", HttpMethod.POST, requestEntity,
                    String.class); // 轮询subversion
            if (response.getStatusCode().is2xxSuccessful()) { // 创建成功
                LOGGER.info(RestLogUtils.packSuccHTTPLogs("创建SCM SVN Repo" + repoName, response));
                String apiResponse = response.getBody();
                JSONObject jb = JSONObject.parseObject(apiResponse);
                if (jb.containsKey(errorMessage)
                        || !(jb.containsKey(message) && jb.containsKey("repository"))) {
                    LOGGER.error("repo creation failed, detailed message is: " + jb.getString(errorMessage)); // 出错了一般是因为已经存在同名repo了
                    LOGGER.warn("End create Subversion Abnormally===");
                    throw new SvnRepoCreateFailureException(REPO_CREATE_FAILED_MSG);
                } else {
                    LOGGER.info("create repo result message = " + jb.getString(message));
                    LOGGER.info("End create Subversion normally===");
                    return jb.getJSONObject("repository").toJavaObject(SubversionRepoDTO.class); // 成功则返回DTO
                }
            } else {
                LOGGER.info(RestLogUtils.packFailHTTPLogs("创建SCM SVN Repo" + repoName, response));
                throw new SvnRepoCreateFailureException(REPO_CREATE_ERROR_MSG);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.warn("End create Subversion Abnormally===");
            throw new SvnRepoCreateFailureException(REPO_CREATE_ERROR_MSG);
        }

    }

    /*
     * 轮询subversion查找所有的template 不返回Exception
     */
    @Override
    public SvnTemplateWrapperDTO findSubversionTemplates() {
        LOGGER.info("===Begin query Subversion for all templates");
        HttpHeaders headers = RestUtils.packBasicAuthHeader(svnConfig.getUsername(), svnConfig.getPassword());
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<SvnTemplateWrapperDTO> response = new ResponseEntity<>(HttpStatus.OK);
        try {
            response = restTemplate.exchange(
                    server.getProtocol() + "://" + server.getOuterServerIpAndPort() + svnConfig.getGenericApi() + "template?format=json&showInactive=true",
                    HttpMethod.GET, request, SvnTemplateWrapperDTO.class);
            if (response.getStatusCode().is2xxSuccessful()) { // 创建成功
                LOGGER.info(RestLogUtils.packSuccHTTPLogs("查询所有Subversion Template Id", response));
            } else {
                LOGGER.error(RestLogUtils.packFailHTTPLogs("查询所有Subversion Template Id", response));
                return new SvnTemplateWrapperDTO();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new SvnTemplateWrapperDTO();
        }
        return response.getBody();
    }

    /*
     * 轮询svnrepo,不抛异常，如果查不到则返回空ArrayList
     */
    @Override
    public List<SubversionRepoDTO> fetchAllSvnRepos() {
        HttpHeaders headers = RestUtils.packBasicAuthHeader(svnConfig.getUsername(), svnConfig.getPassword());
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<SubversionRepoWrapperDTO> response = new ResponseEntity<>(HttpStatus.OK);
        try {
            response = restTemplate.exchange(
                    server.getProtocol() + "://" + server.getOuterServerIpAndPort() + svnConfig.getGenericApi() + "repository?format=json", HttpMethod.GET,
                    request, SubversionRepoWrapperDTO.class); // 轮询subversion
        } catch (Exception e) {
            return new ArrayList<SubversionRepoDTO>();
        }
        SubversionRepoWrapperDTO repos = response.getBody();
        if (response.getStatusCode().is4xxClientError() || repos == null || repos.getRepositories() == null
                || repos.getRepositories().size() < 1) { // 为空，说明subversion现在是空的
            LOGGER.warn(RestLogUtils.packFailHTTPLogs("查询所有Subversion", response));
            LOGGER.info("===End fetchAllSvnRepos");
            return new ArrayList<SubversionRepoDTO>();// 为空当然不存在
        } else {
            LOGGER.info(RestLogUtils.packSuccHTTPLogs("查询所有Subversion", response));
            return repos.getRepositories();
        }

    }

    @Override
    public List<GitRepoDTO> fetchAllGitRepos() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Private-Token", server.getLoginName());
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        List<GitRepoDTO> result = new ArrayList<>();

        String url = server.getProtocol() + "://" + server.getOuterServerIpAndPort() + "/api/v4/projects";
        boolean next;

        do {
            next = false;

            try {
                response = restTemplate.exchange(url.trim(), HttpMethod.GET, request, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Git列表轮询存在合法数据,HTTP返回状态码为: {}, HTTP返回body为: {}"
                        , response.getStatusCode(), response.getBody());
                try {
                    String str = response.getBody();
                    JSONArray jsonArray = JSONObject.parseArray(str);
                    List<GitRepoDTO> list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        GitRepoDTO gitRepoDTO = new GitRepoDTO();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        gitRepoDTO.setId(jsonObject.getLong("id"));
                        gitRepoDTO.setName(jsonObject.getString("path_with_namespace"));
                        gitRepoDTO.setVisibility(jsonObject.getString("visibility"));
                        gitRepoDTO.setHttp_url_to_repo(jsonObject.getString("http_url_to_repo"));
                        gitRepoDTO.setWeb_url(jsonObject.getString("web_url"));
                        gitRepoDTO.setName_with_namespace(jsonObject.getString("name_with_namespace"));
                        list.add(gitRepoDTO);
                    }
                    result.addAll(list);

                    HttpHeaders head = response.getHeaders();
                    List<String> links = head.get("Link");
                    for (String link : links) {
                        String[] linkFirstSplit = link.split(",");
                        for (String sp : linkFirstSplit) {
                            String[] linkSecondSplit = sp.split(";");
                            if (linkSecondSplit.length > 1 && linkSecondSplit[1].contains("next")) {
                                next = true;
                                url = linkSecondSplit[0].replaceAll("[<>]","");
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Query for all git get 200, but parse json error: " + e);
                }
            } else {
                LOGGER.error("Git列表轮询不存在合法数据,HTTP返回状态码为: {}, HTTP返回body为: {}"
                        ,response.getStatusCode(), response.getBody());
            }
        } while (next);

        result.sort((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));

        LOGGER.info("git projects size " + result.size());

        return result;
    }

    @Autowired
    SCMRepoRepository sCMRepoRepository;
	@Override
    public List<ScmRepoInputParam> fetchUnusedSvnRepos() {
        List<SubversionRepoDTO> allSvns = this.fetchAllSvnRepos(); // subversion所有的
        List<SCMRepo> usedSvns = sCMRepoRepository.findAll(); // 本地已经有的
        List<ScmRepoInputParam> retSvns = new ArrayList<>();

//        for (Iterator<SubversionRepoDTO> alljira = allSvns.iterator(); alljira.hasNext();) {
//            SubversionRepoDTO allJiraDto = (SubversionRepoDTO) alljira.next();
//            for (Iterator<SCMRepo> userdjira = usedSvns.iterator(); userdjira.hasNext();) {
//                SCMRepo usedJiraProject = (SCMRepo) userdjira.next();
//                if (allJiraDto.getName().equalsIgnoreCase(usedJiraProject.getRepoName())) {
//                    alljira.remove();
//                    userdjira.remove();
//                    break;
//                }
//            }
//        }
//        for (SubversionRepoDTO svn : allSvns) {
//            retSvns.add(svn.getSCMRepo());
//        }

        Set<String> usedRepoSet = new HashSet<String>();
        for (SCMRepo rep : usedSvns) {
            usedRepoSet.add(rep.getRepoName());
        }
        for (SubversionRepoDTO svn : allSvns) {
            if (!usedRepoSet.contains(svn.getName())) {
                retSvns.add(svn.getSCMRepo());
            }
        }

        return retSvns;
    }

    @Override
    public List<ScmRepoInputParam> fetchUnusedGitRepos() {
        List<GitRepoDTO> allGits = this.fetchAllGitRepos();
        List<SCMRepo> usedSvns = sCMRepoRepository.findAll();
        List<ScmRepoInputParam> scmRepoInputParams = new ArrayList<>();

        Set<String> usedRepoSet = new HashSet<>();
        for (SCMRepo rep : usedSvns) {
            usedRepoSet.add(rep.getRepoName());
        }
        for (GitRepoDTO git : allGits) {
            if (!usedRepoSet.contains(git.getName())) {
                scmRepoInputParams.add(git.getSCMRepo());
            }
        }
        return scmRepoInputParams;
    }

	@Override
	public List<ScmRepoInputParam> fetchAllTemplates() {
		 List<ScmRepoInputParam> retSvns=new ArrayList<>();
		 SvnTemplateWrapperDTO z = this.findSubversionTemplates();
		 for(SvnTemplateDTO dto:z.getTemplates()){
			 retSvns.add(ConversionUtil.Con2SCMRepo(dto));
		 }

		return retSvns ;
	}

	@Override
	public List<SCMRepo> findSvnByProjectId(Long projectId) {
		Project project=projectRepository.findById(projectId).get();
		List<SCMRepo> ret=new ArrayList<>();
		ret.addAll(project.getScmRepositories());
		return ret;
	}

    @Override
    public void setServer(Server server) {
        this.server = server;

    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public void deleteByRepoId(Long repoId) {
        sCMRepoRepository.deleteById(repoId);
    }

    @Override
    public List<ScmRepoInputParam> fetchScmByRepoType(Server server, RepoType repoType) {
        List<ScmRepoInputParam> result = null;
        this.server = server;
        switch (repoType) {
            case SVN:
                result = fetchUnusedSvnRepos();
                break;
            case GIT:
                result = fetchUnusedGitRepos();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public GitRepoDTO createGitRepo(String repoName) throws Exception {
        LOGGER.info("===Begin create Git, name = " + repoName);
        boolean isExist = checkIfGitRepoExists(repoName); // 存在吗？
        if (isExist) {
            LOGGER.warn("End create Git Abnormally==="); // 存在就不能创建了
            throw new SvnRepoCreateFailureException(REPO_ALREADY_EXIST_MSG);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Private-Token", server.getLoginName());
        ResponseEntity<String> response;
        // 这里存放Post请求的入参Map
        Map<String, Object> params = new HashMap<>(2,0.75f);
        params.put("name", repoName);
//        if (applyTemplateId > 1) { // 传入有效的TemplateId，方可指定模板，输入负数表示越过模板采用subversion默认 传1会有400错误
//            params.put("applyTemplateId", applyTemplateId);
//        }
//        params.put("applyStandardLayout", applyStandardLayout); // true代表branch/tag/trunk风格，false代表裸
//        String requestBody = JSONObject.toJSONString(params);
//        HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, headers);
//
//        try {
//            response = restTemplate.exchange(server.getProtocol() + "://" + server.getOuterServerIpAndPort()
//                            + svnConfig.getGenericApi() + "repository?format=json", HttpMethod.POST, requestEntity,
//                    String.class); // 轮询subversion
//            if (response.getStatusCode().is2xxSuccessful()) { // 创建成功
//                LOGGER.info(RestLogUtils.packSuccHTTPLogs("创建SCM SVN Repo" + repoName, response));
//                String apiResponse = response.getBody();
//                JSONObject jb = JSONObject.parseObject(apiResponse);
//                if (jb.containsKey(errorMessage)
//                        || !(jb.containsKey(message) && jb.containsKey("repository"))) {
//                    LOGGER.error("repo creation failed, detailed message is: " + jb.getString(errorMessage)); // 出错了一般是因为已经存在同名repo了
//                    LOGGER.warn("End create Subversion Abnormally===");
//                    throw new SvnRepoCreateFailureException(REPO_CREATE_FAILED_MSG);
//                } else {
//                    LOGGER.info("create repo result message = " + jb.getString(message));
//                    LOGGER.info("End create Subversion normally===");
//                    return jb.getJSONObject("repository").toJavaObject(SubversionRepoDTO.class); // 成功则返回DTO
//                }
//            } else {
//                LOGGER.info(RestLogUtils.packFailHTTPLogs("创建SCM SVN Repo" + repoName, response));
//                throw new SvnRepoCreateFailureException(REPO_CREATE_ERROR_MSG);
//            }
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//            LOGGER.warn("End create Subversion Abnormally===");
//            throw new SvnRepoCreateFailureException(REPO_CREATE_ERROR_MSG);
//        }
        return null;
    }

    private boolean checkIfGitRepoExists(String repoName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Private-Token", server.getLoginName());
        return false;
    }

}
