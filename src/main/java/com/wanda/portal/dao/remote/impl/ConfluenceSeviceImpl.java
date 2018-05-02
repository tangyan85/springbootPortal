package com.wanda.portal.dao.remote.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanda.portal.config.biz.ConfluenceConfig;
import com.wanda.portal.constants.ConfluenceConstants;
import com.wanda.portal.dao.jpa.ConfluenceSpaceRepository;
import com.wanda.portal.dao.remote.ConfluenceService;
import com.wanda.portal.dto.confluence.CreateConfluenceSpaceParamDTO;
import com.wanda.portal.dto.confluence.GenericConfluenceSpaceDTO;
import com.wanda.portal.dto.jenkins.JenkinsJobDTO;
import com.wanda.portal.entity.ConfluenceSpace;
import com.wanda.portal.entity.JenkinsProject;
import com.wanda.portal.entity.Server;
import com.wanda.portal.exception.ConfluenceSpaceCreateFailureException;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;
import com.wanda.portal.utils.RestLogUtils;
import com.wanda.portal.utils.RestUtils;

@Primary
@Service("ConfluenceSeviceImpl")
@Scope("prototype")
public class ConfluenceSeviceImpl implements ConfluenceService {
    private static Logger LOGGER = LoggerFactory.getLogger(ConfluenceSeviceImpl.class);

    @Autowired
    ConfluenceConfig confluenceConfig;
    @Autowired
    RestTemplate restTemplate;

    private Server server;
    
    /*
     * { "key" : "ZZRR", "name" : "打时空的金卡", "description": { "plain": {
     * "representation":"software-project", "value": "very good" } } }
     */
    @Override
    public String createSpace(CreateConfluenceSpaceParamDTO confluenceSpace) throws Exception {
        HttpHeaders headers =
                RestUtils.packBasicAuthHeader(confluenceConfig.getUsername(), confluenceConfig.getPassword());
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        List<MediaType> accps = new ArrayList<>();
        accps.add(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(accps);
        Map<String, Object> confInputParam = packCreateConfluenceParam(confluenceSpace);

        HttpEntity<String> requestEntity = new HttpEntity<String>(JSONObject.toJSONString(confInputParam), headers);
        ResponseEntity<String> response;
        try {
            response =
                    restTemplate.exchange(
                            server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                                    + confluenceConfig.getGenericApi() + "/" + ConfluenceConstants.space,
                            HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) { // 创建成功
                LOGGER.info(RestLogUtils.packSuccHTTPLogs("ConfluenceSpace项目" + confluenceSpace + "创建", response));
                JSONObject jb = JSONObject.parseObject(response.getBody());
                String webui = jb.getJSONObject("homepage").getJSONObject("_links").getString("webui");
                return confluenceConfig.getUrlHead() + webui;
            } else {
                LOGGER.error(RestLogUtils.packSuccHTTPLogs("ConfluenceSpace项目" + confluenceSpace + "创建", response));
                throw new ConfluenceSpaceCreateFailureException("confluence create error!");
            }
        } catch (RestClientException e) {
            LOGGER.error(
                    "Failed to create confluence space due to rest errors, the error is: " + e.getLocalizedMessage());
            throw new ConfluenceSpaceCreateFailureException("call confluence error!");
        } catch (Exception ex) {
            LOGGER.error(
                    "Failed to create confluence space due to other errors, the error is: " + ex.getLocalizedMessage());
            throw new ConfluenceSpaceCreateFailureException("call confluence error!");
        }
    }

    private static Map<String, Object> packCreateConfluenceParam(CreateConfluenceSpaceParamDTO confluenceSpace) {
        Map<String, String> plain = new HashMap<>();
        plain.put(ConfluenceConstants.representation, confluenceSpace.getRepresentation());
        plain.put(ConfluenceConstants.value, confluenceSpace.getDescription());
        Map<String, Object> description = new HashMap<>();
        description.put(ConfluenceConstants.plain, plain);
        Map<String, Object> confInputParam = new HashMap<>();
        confInputParam.put(ConfluenceConstants.key, confluenceSpace.getKey());
        confInputParam.put(ConfluenceConstants.name, confluenceSpace.getName());
        confInputParam.put(ConfluenceConstants.description, description);
        return confInputParam;
    }

    @Deprecated
    @Override
    public ConfluenceSpace getConfluenceSpaceByKey(String spaceKey) {
        return null;
    }

    /*
     * 此方法轮询所有ConfluenceSpace，不返还Exception，目的是为了安全的轮询一下Confluence获得所有Space
     */
    @Override
    public List<GenericConfluenceSpaceDTO> fetchAllConfluenceSpaces() {
        HttpHeaders headers =
                RestUtils.packBasicAuthHeader(confluenceConfig.getUsername(), confluenceConfig.getPassword());
        //List<Charset> acceptableCharsets = new ArrayList<>();
        //acceptableCharsets.add(Charset.defaultCharset());
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes );
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        try {
            response =
                    restTemplate.exchange(
                            server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                                    + confluenceConfig.getGenericApi() + "/" + ConfluenceConstants.space,
                            HttpMethod.GET, request, String.class); // 轮询Confluence Space
        } catch (Exception e) {
            LOGGER.error("Query for all confluence spaces error: " + e);
            return new ArrayList<GenericConfluenceSpaceDTO>();
        }
        if (response.getStatusCode().is2xxSuccessful()) { // 说明有Confluence Space
            LOGGER.info("ConfluenceSpace列表轮询存在合法数据,HTTP返回状态码为: " + response.getStatusCode() + "HTTP返回body为: "
                    + response.getBody());
            try {
                JSONObject jb = JSONObject.parseObject(response.getBody());
                JSONArray re = jb.getJSONArray("results");
                List<GenericConfluenceSpaceDTO> res =
                        JSONObject.parseArray(re.toJSONString(), GenericConfluenceSpaceDTO.class);
                LOGGER.info("获得的Confluence Space为：" + res);
                return res;
            } catch (Exception e) {
                LOGGER.error("Query for all confluence spaces get 200, but parse json error: " + e);
                return new ArrayList<GenericConfluenceSpaceDTO>();
            }
        } else {
            LOGGER.error("ConfluenceSpace列表轮询不存在合法数据,HTTP返回状态码为: " + response.getStatusCode() + "HTTP返回body为: "
                    + response.getBody());
            return new ArrayList<GenericConfluenceSpaceDTO>();
        }
    }
    
    @Autowired
    ConfluenceSpaceRepository confluenceSpaceRepository;
    @Override
    public List<ConfluenceSpaceInputParam> fetchUnusedConfs() {
        List<GenericConfluenceSpaceDTO> allConfs = this.fetchAllConfluenceSpaces();
        List<ConfluenceSpace> usedConfs = confluenceSpaceRepository.findAll();
        List<ConfluenceSpaceInputParam> retConfs = new ArrayList<>();

//        for (Iterator<GenericConfluenceSpaceDTO> alljira = allConfs.iterator(); alljira.hasNext();) {
//            GenericConfluenceSpaceDTO allJiraDto = (GenericConfluenceSpaceDTO) alljira.next();
//            for (Iterator<ConfluenceSpace> userdjira = usedConfs.iterator(); userdjira.hasNext();) {
//                ConfluenceSpace usedJiraProject = (ConfluenceSpace) userdjira.next();
//                if (allJiraDto.getKey().equalsIgnoreCase(usedJiraProject.getSpaceKey())) {
//                    alljira.remove();
//                    userdjira.remove();
//                    break;
//                }
//            }
//        }
//
//        for (GenericConfluenceSpaceDTO conf : allConfs) {
//            retConfs.add(conf.getConfluenceSpace());
//        }
        
        Set<String> usedConfSet = new HashSet<String>();
        for (ConfluenceSpace usd : usedConfs) {
            usedConfSet.add(usd.getSpaceKey());
        }
        for (GenericConfluenceSpaceDTO conf : allConfs) {
            if (!usedConfSet.contains(conf.getKey())) {
                retConfs.add(conf.getConfluenceSpace());
            }
        }
        return retConfs;
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
