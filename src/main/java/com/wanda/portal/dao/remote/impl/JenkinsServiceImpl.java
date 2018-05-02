package com.wanda.portal.dao.remote.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanda.portal.config.biz.JenkinsConfig;
import com.wanda.portal.constants.JenkinsConstants;
import com.wanda.portal.dao.jpa.JenkinsProjectRepository;
import com.wanda.portal.dao.remote.JenkinsService;
import com.wanda.portal.dto.jenkins.JenkinsJobDTO;
import com.wanda.portal.dto.svn.SubversionRepoDTO;
import com.wanda.portal.entity.JenkinsProject;
import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.entity.Server;
import com.wanda.portal.exception.JenkinsJobCreateFailureException;
import com.wanda.portal.facade.model.input.JenkinsInputParam;
import com.wanda.portal.utils.RestLogUtils;
import com.wanda.portal.utils.RestUtils;

@Primary
@Service("JenkinsServiceImpl")
@Scope("prototype")
public class JenkinsServiceImpl implements JenkinsService {
    private static Logger LOGGER = LoggerFactory.getLogger(JenkinsServiceImpl.class);
    @Autowired
    JenkinsConfig jenkinsConfig;
    @Autowired
    RestTemplate restTemplate;

    private Server server;
    
    @Override
    public List<JenkinsJobDTO> fetchAllJenkinsJobs() {
        HttpHeaders headers = RestUtils.packBasicAuthHeader(jenkinsConfig.getUsername(), jenkinsConfig.getPassword());
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        List<MediaType> accps = new ArrayList<>();
        accps.add(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(accps);
        HttpEntity<String> requestEntity = new HttpEntity<String>(JenkinsConstants.EMPTY_JSON_PARAM, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                    + jenkinsConfig.getJsonQueryJobsApi(), HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) { // 创建成功
                LOGGER.info(RestLogUtils.packSuccHTTPLogs("获得所有JenkinsJobs", response));
                JSONObject jb = JSONObject.parseObject(response.getBody());
                JSONArray ja = jb.getJSONArray("jobs");
                return ja.toJavaList(JenkinsJobDTO.class);
            } else {
                LOGGER.info(RestLogUtils.packFailHTTPLogs("获得所有JenkinsJobs", response));
                return new ArrayList<JenkinsJobDTO>();
            }
        } catch (Exception e) {
            LOGGER.info("fetchAllJenkinsJobs失败,错误码为: " + e.getLocalizedMessage());
            return new ArrayList<JenkinsJobDTO>();
        }
    }

    @Override
    public void createJenkinsUsingCopy(String newName, String fromName) throws Exception {
        LOGGER.info("===Begin creating jenkins job from old job: " + fromName + " using new name: " + newName);
        HttpHeaders headers = RestUtils.packBasicAuthHeader(jenkinsConfig.getUsername(), jenkinsConfig.getPassword());
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        List<MediaType> accps = new ArrayList<>();
        accps.add(MediaType.TEXT_HTML);
        headers.setAccept(accps);
        HttpEntity<String> requestEntity = new HttpEntity<String>(JenkinsConstants.EMPTY_JSON_PARAM, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(packCreateUrl(newName, fromName), HttpMethod.POST, requestEntity,
                    String.class);
            if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection()) { // 创建成功，不一定是200系列HTTP码
                LOGGER.info(RestLogUtils.packSuccHTTPLogs("Jenkins项目创建", response));
                return;
            } else {
                LOGGER.error(RestLogUtils.packFailHTTPLogs("Jenkins项目创建", response));
                throw new JenkinsJobCreateFailureException("Jenkins job create error!");
            }
        } catch (RestClientException e) {
            popException(e);
        } catch (Exception ex) {
            popException(ex);
        }

    }
    
    private static void popException(Exception e) throws JenkinsJobCreateFailureException {
        LOGGER.error("Failed to create confluence space due to rest errors, the error is: " + e.getLocalizedMessage());
        throw new JenkinsJobCreateFailureException("call jenkins error! with error: " + e.getLocalizedMessage());
    }

    private String packCreateUrl(String newName, String fromName) {
        return server.getProtocol() + "://" + server.getOuterServerIpAndPort() + jenkinsConfig.getCreateItem() + "?"
                + JenkinsConstants.name + "=" + newName + "&" + JenkinsConstants.modecopy + "&" + JenkinsConstants.from
                + "=" + fromName;
    }
    
    @Autowired
    JenkinsProjectRepository jenkinsProjectRepository;
    @Override
    public List<JenkinsInputParam> fetchUnusedJekins() {
        List<JenkinsJobDTO> allJenkins = this.fetchAllJenkinsJobs();
        List<JenkinsProject> usedJenkins = jenkinsProjectRepository.findAll();
        List<JenkinsInputParam> retJenkins = new ArrayList<>();
//
//        for (Iterator<JenkinsJobDTO> alljira = allJenkins.iterator(); alljira.hasNext();) {
//            JenkinsJobDTO allJiraDto = (JenkinsJobDTO) alljira.next();
//            for (Iterator<JenkinsProject> userdjira = usedJenkins.iterator(); userdjira.hasNext();) {
//                JenkinsProject usedJiraProject = (JenkinsProject) userdjira.next();
//                if (allJiraDto.getName().equalsIgnoreCase(usedJiraProject.getJenkinsProjKey())) {
//                    alljira.remove();
//                    userdjira.remove();
//                    break;
//                }
//            }
//        }
//        for (JenkinsJobDTO jenkin : allJenkins) {
//            retJenkins.add(jenkin.getJenkinsProject());
//        }
//        
        Set<String> usedJenkinsSet = new HashSet<String>();
        for (JenkinsProject usd : usedJenkins) {
            usedJenkinsSet.add(usd.getJenkinsProjKey());
        }
        for (JenkinsJobDTO jenk : allJenkins) {
            if (!usedJenkinsSet.contains(jenk.getName())) {
                retJenkins.add(jenk.getJenkinsProject());
            }
        }
        
        return retJenkins;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public Server getServer() {
        return this.server;
    }

}
