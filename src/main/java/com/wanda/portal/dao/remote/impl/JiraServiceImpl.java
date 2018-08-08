package com.wanda.portal.dao.remote.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.wanda.portal.config.biz.JiraConfig;
import com.wanda.portal.constants.JiraConstants;
import com.wanda.portal.dao.jpa.JiraProjectRepository;
import com.wanda.portal.dao.remote.AbstractRestService;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dto.jira.*;
import com.wanda.portal.entity.JiraProject;
import com.wanda.portal.entity.Server;
import com.wanda.portal.exception.JiraProjectCreateFailureException;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
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
@Service("JiraServiceImpl")
@Scope
public class JiraServiceImpl extends AbstractRestService implements JiraService {
    private static Logger logger = LoggerFactory.getLogger(JiraServiceImpl.class);
    private static final String JIRA_ALREADY_EXISTS_ERROR_MSG = "jira project already exists";
    private static final String JIRA_CREATE_ERROR_MSG = "error occurred during creating for jira project";
    @Autowired
    JiraConfig jiraConfig;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JiraProjectRepository jiraProjectRepository;

    /*
     * 入参1 JiraInputDTO 入参2 isCheckExist是否事先检查jira的KEY是否存在
     */
    @Override
    public JiraOutputDTO createJiraProjectUsingTemplate(JiraInputDTO dto, boolean isCheckExist, Server server) throws Exception {
        logger.info("===Begin create Jira project" + JSONObject.toJSONString(dto) + "isCheckExist:" + isCheckExist);

        if (isCheckExist) { // 是否事先检查jira项目已经存在？
            boolean isExist = checkIfJiraProjectExist(dto.getKey(), server);
            if (isExist) { // 存在，则抛异常，创建失败
                throw new JiraProjectCreateFailureException(JIRA_ALREADY_EXISTS_ERROR_MSG);
            }
        }

        HttpHeaders headers = RestUtils.packBasicAuthHeader(jiraConfig.getUsername(), jiraConfig.getPassword());
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jiraInputParam = JSONObject.toJSONString(dto);
        HttpEntity<String> requestEntity = new HttpEntity<String>(jiraInputParam, headers);
        ResponseEntity<String> response =
                restTemplate.exchange(server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                        + jiraConfig.getGenericApi() + "/project", HttpMethod.POST, requestEntity, String.class);

        String apiResponse = response.getBody();
        logger.info("response +" + apiResponse);
        if (response.getStatusCode().is2xxSuccessful()) {
            JiraOutputDTO res = JSONObject.parseObject(apiResponse, JiraOutputDTO.class);
            logger.info(RestLogUtils.packSuccHTTPLogs("Create Jira： " + dto, response));
            logger.info("===End create Jira project normally");
            return res;
        } else {
            logger.info(RestLogUtils.packFailHTTPLogs("Create Jira： " + dto, response));
            logger.warn("End create Jira project Abnormally===");
            throw new JiraProjectCreateFailureException(JIRA_CREATE_ERROR_MSG);
        }

    }

    /*
     * 项目的密钥必须以大写字母开头, 后跟一条或更多的大写字母数字字符
     */
    @Override
    public boolean checkIfJiraProjectExist(String jiraKey, Server server) throws Exception {
        if (!RegexUtils.isValidJiraProject(jiraKey)) { // 如果不合法的jiraKey，直接抛异常
            throw new JiraProjectCreateFailureException("ILLEGAL JIRA NAME " + jiraKey);
        }
        return rawCheckExistence(jiraKey, server);
    }

    /*
     * 轮询JIRA Project的key或id是否存在
     */
    private boolean rawCheckExistence(String jiraKeyOrId, Server server) {
        HttpHeaders headers = RestUtils.packBasicAuthHeader(jiraConfig.getUsername(), jiraConfig.getPassword());
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        try {
            response =
                    restTemplate.exchange(
                            server.getProtocol() + "://" + server.getOuterServerIpAndPort() + jiraConfig.getGenericApi()
                                    + "/" + JiraConstants.project + "/" + jiraKeyOrId,
                            HttpMethod.GET, request, String.class); // 轮询jira
        } catch (Exception e) {
            return false;
        }
        if (response.getStatusCode().is2xxSuccessful()) { // 找到了，说明存在
            logger.info(RestLogUtils.packSuccHTTPLogs("JIRA项目" + jiraKeyOrId + "存在", response));
            return true;
        } else {
            logger.info(RestLogUtils.packFailHTTPLogs("JIRA项目" + jiraKeyOrId + "不存在", response));
            return false;
        }
    }

    /*
     * 此方法轮询所有Project，不返还Exception，目的是为了安全的轮询一下JIRA获得所有Project
     */
    @Override
    public List<GenericJiraProjectDTO> fetchAllJiraProjects(Server server) {
        HttpHeaders headers = RestUtils.packBasicAuthHeader(server.getLoginName(), server.getPasswd());
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                    + jiraConfig.getGenericApi() + "/" + JiraConstants.project, HttpMethod.GET, request, String.class); // 轮询jira
        } catch (Exception e) {
            return new ArrayList<GenericJiraProjectDTO>();
        }
        if (response.getStatusCode().is2xxSuccessful()) { // 说明有Project
            logger.info(RestLogUtils.packSuccHTTPLogs("JIRA项目Project列表轮询存在合法数据", response));
            List<GenericJiraProjectDTO> res = JSONObject.parseArray(response.getBody(), GenericJiraProjectDTO.class);
            logger.info("获得的项目列表为：" + res);
            return res;
        } else {
            logger.error(RestLogUtils.packFailHTTPLogs("JIRA项目Project列表轮询不存在合法数据", response));
            return new ArrayList<GenericJiraProjectDTO>();
        }
    }

    @Override
    public Integer fetchProjectAllIssues(final String projectId, Server server) {
        Map<String, String> values = RestUtils.basicAuthHeader(server.getLoginName(), server.getPasswd());
        String url = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + jiraConfig.getGenericApi() + "/search?jql=project=" + projectId;
        return restRequest(values, "{}", url, HttpMethod.GET, (t) ->
        {
            JSONObject jb = JSONObject.parseObject(t);
            return (Integer) JSONPath.eval(jb, "$.total");
        });
    }

    @Override
    public Integer fetchProjectFinishIssues(final String projectId, Server server) {
        Map<String, String> values = RestUtils.basicAuthHeader(server.getLoginName(), server.getPasswd());
        String url = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + jiraConfig.getGenericApi() + "/search?jql=project=" + projectId +" AND status = 5";
        return restRequest(values, "{}", url, HttpMethod.GET, (t) ->
        {
            JSONObject jb = JSONObject.parseObject(t);
            return (Integer) JSONPath.eval(jb, "$.total");
        });
    }

    @Override
    public List<JiraProjectVersionDTO> fetchProjectVersions(final String projectId, Server server) {
        Map<String, String> values = RestUtils.basicAuthHeader(server.getLoginName(), server.getPasswd());
        String url = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + jiraConfig.getGenericApi() + "/project/" + projectId + "/versions";
        return restRequest(values, "{}", url, HttpMethod.GET, (t) ->
                JSONObject.parseArray(t, JiraProjectVersionDTO.class));
    }

    @Override
    public List<JiraProjectComponentDTO> fetchProjectComponents(final String projectId, Server server) {
        Map<String, String> values = RestUtils.basicAuthHeader(server.getLoginName(), server.getPasswd());
        String url = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + jiraConfig.getGenericApi() + "/project/" + projectId + "/components";

        return restRequest(values, "{}", url, HttpMethod.GET, (t) ->
        {
            JSONArray ja = JSONObject.parseArray(t);
            List<JiraProjectComponentDTO> list = new ArrayList<>();
            for (Object o : ja) {
                JiraProjectComponentDTO dto = new JiraProjectComponentDTO();
                Map<String, Object> map = (Map<String, Object>) o;
                Object obj = map.get("realAssignee");
                if (obj != null) {
                    Map<String, Object> map2 = (Map<String, Object>) obj;
                    dto.setDisplayName(map2.get("displayName").toString());
                }
                dto.setId(Long.valueOf(map.get("id").toString()));
                String reUrl = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                        + jiraConfig.getGenericApi() + "/component/" + dto.getId() + "/relatedIssueCounts";
                Integer issueCount = restRequest(values, "{}", reUrl, HttpMethod.GET, (t1) -> {
                    JSONObject jb = JSONObject.parseObject(t1);
                    return (Integer) JSONPath.eval(jb, "$.issueCount");
                });
                dto.setIssues(issueCount);
                dto.setName(map.get("name").toString());
                String self = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                        + "/issues/?jql=project %3D " + projectId + " AND component %3D " + dto.getName();
                dto.setSelf(self);
                dto.setDescription(map.get("description").toString());
                list.add(dto);
            }
            return  list;
        });
    }

    @Override
    public JiraProject findById(String jiraProjectId) {
        return jiraProjectRepository.findById(Long.valueOf(jiraProjectId)).get();
    }

    @Override
    public Long createJiraProjectUsingExistingProject(Long existingProjectId, String key, String leader,
                                                      String projectName, boolean isCheckExist, Server server) throws Exception {
        if (isCheckExist) {
            boolean templateIsExist = rawCheckExistence(existingProjectId.toString(), server);
            if (!templateIsExist) { // 模板Id不存在，肯定不支持创建
                throw new JiraProjectCreateFailureException(
                        "template project id: " + existingProjectId + " not exists!");
            }
            boolean keyIsExist = checkIfJiraProjectExist(key, server);
            if (keyIsExist) { // 待创建key存在，也不支持创建
                throw new JiraProjectCreateFailureException("project key to be created: " + key + " already exists!");
            }
        }

        HttpHeaders headers = RestUtils.packBasicAuthHeader(jiraConfig.getUsername(), jiraConfig.getPassword());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> dto = new HashMap<>();
        dto.put("key", key);
        dto.put("lead", leader);
        dto.put("name", projectName);
        String jiraInputParam = JSONObject.toJSONString(dto);
        HttpEntity<String> requestEntity = new HttpEntity<String>(jiraInputParam, headers);
        ResponseEntity<String> response = restTemplate.exchange(server.getProtocol() + "://"
                        + server.getOuterServerIpAndPort() + jiraConfig.getOldCreateApi() + "/" + existingProjectId,
                HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info(
                    "JIRA项目Project创建成功,HTTP返回状态码为: " + response.getStatusCode() + "HTTP返回body为: " + response.getBody());
            JSONObject jb = JSONObject.parseObject(response.getBody());
            logger.info("===End create Jira project normally");
            return jb.getLong("projectId");
        } else {
            logger.error("create failed");
            logger.warn("End create Jira project Abnormally===");
            throw new JiraProjectCreateFailureException(JIRA_CREATE_ERROR_MSG);
        }

    }

    @Override
    public String loginJira(String username, String password, Server server) throws Exception {

        HttpHeaders headers = RestUtils.packBasicAuthHeader(jiraConfig.getUsername(), jiraConfig.getPassword());
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes);
        Map<String, Object> dto = new HashMap<>();
        dto.put("username", username);
        dto.put("password", password);
        String jiraInputParam = JSONObject.toJSONString(dto);
        HttpEntity<String> requestEntity = new HttpEntity<String>(jiraInputParam, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                server.getProtocol() + "://" + server.getOuterServerIpAndPort() + "/rest/auth/1/session",
                HttpMethod.POST, requestEntity, String.class);

        logger.info(response.getBody());
        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("JIRA登录成功,HTTP返回状态码为: " + response.getStatusCode() + "HTTP返回body为: " + response.getBody());
        } else {
            logger.error("create failed");
            throw new JiraProjectCreateFailureException(JIRA_CREATE_ERROR_MSG);
        }
        return response.getStatusCode().toString();
    }

    @Override
    public List<JiraProjectInputParam> fetchUnusedJiraProject(Server server) {
        List<GenericJiraProjectDTO> allJiras = this.fetchAllJiraProjects(server);
        List<JiraProject> usedJiras = jiraProjectRepository.findAll();
        List<JiraProjectInputParam> retJiras = new ArrayList<>();

//        for (Iterator<GenericJiraProjectDTO> alljira = allJiras.iterator(); alljira.hasNext();) {
//            GenericJiraProjectDTO allJiraDto = (GenericJiraProjectDTO) alljira.next();
//            for (Iterator<JiraProject> userdjira = usedJiras.iterator(); userdjira.hasNext();) {
//                JiraProject usedJiraProject = (JiraProject) userdjira.next();
//                if (allJiraDto.getKey().equalsIgnoreCase(usedJiraProject.getJiraProjectKey())) {
//                    alljira.remove();
//                    userdjira.remove();
//                    break;
//                }
//            }
//        }
//        for (GenericJiraProjectDTO jira : allJiras) {
//            retJiras.add(jira.getJiraProject());
//        }

        Set<String> usedJirasSet = new HashSet<String>();
        for (JiraProject usd : usedJiras) {
            usedJirasSet.add(usd.getJiraProjectKey());
        }
        for (GenericJiraProjectDTO jira : allJiras) {
            if (!usedJirasSet.contains(jira.getKey())) {
                retJiras.add(jira.getJiraProject());
            }
        }

        return retJiras;
    }

    @Override
    public void deleteByJiraId(Long jiraId) {
        jiraProjectRepository.deleteById(jiraId);
    }
}
