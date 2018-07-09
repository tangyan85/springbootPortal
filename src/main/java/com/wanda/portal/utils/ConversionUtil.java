package com.wanda.portal.utils;

import com.wanda.portal.constants.Constants;
import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.dto.confluence.GenericConfluenceSpaceDTO;
import com.wanda.portal.dto.git.GitRepoDTO;
import com.wanda.portal.dto.jenkins.JenkinsJobDTO;
import com.wanda.portal.dto.jira.GenericJiraProjectDTO;
import com.wanda.portal.dto.svn.SubversionRepoDTO;
import com.wanda.portal.dto.svn.SvnTemplateDTO;
import com.wanda.portal.entity.*;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;
import com.wanda.portal.facade.model.input.JenkinsInputParam;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import com.wanda.portal.facade.model.output.ServerOutputParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 对象转换工具类
 * 
 * @author hp
 *
 */
public class ConversionUtil {
	public static List<Project> Con2Project(List<Project> inputDto){
		List<Project>outputDto=new ArrayList<>();
		for(Project p:outputDto){
			outputDto.add(Con2Project(p));
		}
		return outputDto;
	}
	public static Project Con2Project(Project inputDto) {
		Project outputDto = inputDto;
		for(JiraProject s:outputDto.getJiraProjects()){
			s.setInputActionType(InputActionType.UPDATE_OR_NOTHING);
		}
		if(outputDto.getJiraProjects()!=null&&outputDto.getJiraProjects().size()==0){
			Set<JiraProject>  jiras=new HashSet<>();
			JiraProject jira=new JiraProject();
			jira.setInputActionType(InputActionType.UPDATE_OR_NOTHING);
			jiras.add(jira);
			outputDto.setJiraProjects(jiras);
		}
		for(ConfluenceSpace s:outputDto.getConfluenceSpaces()){
			s.setInputActionType(InputActionType.UPDATE_OR_NOTHING);
		}
		for(JenkinsProject s:outputDto.getJenkinsProjects()){
			s.setInputActionType(InputActionType.UPDATE_OR_NOTHING);
		}
		for(SCMRepo s:outputDto.getScmRepositories()){
			s.setInputActionType(InputActionType.UPDATE_OR_NOTHING);
		}
		return outputDto;
	}
	
	public static ServerOutputParam Con2ServerOutputParam(Server inputDto) {
		ServerOutputParam outputDto = new ServerOutputParam();
		BeanUtils.copyProperties(inputDto, outputDto);
		if (outputDto != null) {
			StringBuilder sb=new StringBuilder();
			sb.append(outputDto.getProtocol());
			sb.append(Constants.URL_PROTOCOL_SPEATOR);
			sb.append(Constants.URL_PATH_SPEATOR);
			sb.append(Constants.URL_PATH_SPEATOR);
			
				if(StringUtils.isNotBlank(outputDto.getDomain())){
					sb.append(outputDto.getDomain());
				}else{
					sb.append(outputDto.getOuterServerIpAndPort());
				}
				outputDto.setUrlContext(sb.toString());
				if(StringUtils.isNotBlank(outputDto.getServerRoot())){
					sb.append(Constants.URL_PATH_SPEATOR);
					sb.append(outputDto.getServerRoot());
				}
				outputDto.setUrlPrefix(sb.toString());
				if(StringUtils.isNotBlank(outputDto.getLoginSuffix())){
					if(!outputDto.getLoginSuffix().startsWith(Constants.URL_PATH_SPEATOR)){
						sb.append(Constants.URL_PATH_SPEATOR);
					}
					sb.append(outputDto.getLoginSuffix());
				}
				outputDto.setLoginUrl(sb.toString());

		}
		return outputDto;
	}
	
	/**
	 * 
	 * @param GenericJiraProjectDTO
	 * @return JiraProject
	 */
	public static JiraProjectInputParam Con2JiraProject(
			GenericJiraProjectDTO inputDto, String serverIP) {
		JiraProjectInputParam outputDto = new JiraProjectInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getKey())) {
				outputDto.setJiraProjectKey(inputDto.getKey());
			}
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setJiraProjectName(inputDto.getName());
			}
			// if (StringUtils.isNotEmpty(serverIP)) {
			// outputDto.setServerIP(serverIP);
			// }
			outputDto.setReferJiraId(inputDto.getId());
			inputDto.setAvatarUrls(null);
			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	public static JiraProjectInputParam Con2JiraProject(
			GenericJiraProjectDTO inputDto) {
		JiraProjectInputParam outputDto = new JiraProjectInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getKey())) {
				outputDto.setJiraProjectKey(inputDto.getKey());
			}
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setJiraProjectName(inputDto.getName());
			}
			outputDto.setReferJiraId(inputDto.getId());
			inputDto.setAvatarUrls(null);
			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	/**
	 * 
	 * @param GenericConfluenceSpaceDTO
	 * @return ConfluenceSpace
	 */
	public static ConfluenceSpaceInputParam Con2ConfluenceSpace(
			GenericConfluenceSpaceDTO inputDto, String serverIP) {
		ConfluenceSpaceInputParam outputDto = new ConfluenceSpaceInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getKey())) {
				outputDto.setSpaceKey(inputDto.getKey());
			}
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setSpaceName(inputDto.getName());
			}
			if (StringUtils.isNotEmpty(serverIP)) {
				outputDto.setServerIP(serverIP);
			}
			outputDto.setPageId(inputDto.getId().toString());
			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	public static ConfluenceSpaceInputParam Con2ConfluenceSpace(
			GenericConfluenceSpaceDTO inputDto) {
		ConfluenceSpaceInputParam outputDto = new ConfluenceSpaceInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getKey())) {
				outputDto.setSpaceKey(inputDto.getKey());
			}
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setSpaceName(inputDto.getName());
			}
			outputDto.setPageId(inputDto.getId().toString());
			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	/**
	 * 
	 * @param JenkinsJobDTO
	 * @return JenkinsProject
	 */
	public static JenkinsInputParam Con2JenkinsProject(JenkinsJobDTO inputDto,
			String serverIP) {
		JenkinsInputParam outputDto = new JenkinsInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setJenkinsProjKey(inputDto.getName());
				outputDto.setReferProj(inputDto.getName());
			}
			if (StringUtils.isNotEmpty(serverIP)) {
				outputDto.setServerIP(serverIP);
			}
			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	public static JenkinsInputParam Con2JenkinsProject(JenkinsJobDTO inputDto) {
		JenkinsInputParam outputDto = new JenkinsInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setJenkinsProjKey(inputDto.getName());
				outputDto.setReferProj(inputDto.getName());
				outputDto.setJenkinsProjUrl(inputDto.getUrl());
			}
			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	/**
	 * 
	 * @param SubversionRepoDTO
	 * @return JenkinsProject
	 * @throws Exception
	 */
	public static ScmRepoInputParam Con2SCMRepo(SubversionRepoDTO inputDto,
			String serverIP) {
		ScmRepoInputParam outputDto = new ScmRepoInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setRepoName(inputDto.getName());
			}
			if (StringUtils.isNotEmpty(serverIP)) {
				outputDto.setServerIP(serverIP);
			}
			// if (StringUtils.isNotEmpty(inputDto.getSvnUrl())) {
			// outputDto.setServerIP(UrlUtil.getHost(inputDto.getSvnUrl()));
			// outputDto
			// .setServerRoot(UrlUtil.getContext(inputDto.getSvnUrl()));
			// outputDto.setProtocol(RepoProtocol.valueOf(UrlUtil.getProtocol(
			// inputDto.getSvnUrl()).toUpperCase()));
			// }
			// if (StringUtils.isNotEmpty(inputDto.getViewvcUrl())) {
			// outputDto.setBrowsingServerIP(UrlUtil.getHost(inputDto
			// .getViewvcUrl()));
			// outputDto.setBrowsingServerRoot(UrlUtil.getContext(inputDto
			// .getViewvcUrl()));
			// }
			if (null != outputDto.getRepoType()) {
				outputDto.setRepoType(RepoType.SVN);
			}
			outputDto.setRepoRemoteId(inputDto.getId());

			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	public static ScmRepoInputParam Con2SCMRepo(SubversionRepoDTO inputDto) {
		ScmRepoInputParam outputDto = new ScmRepoInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setRepoName(inputDto.getName());
			}
			// if (StringUtils.isNotEmpty(inputDto.getSvnUrl())) {
			// outputDto.setServerIP(UrlUtil.getHost(inputDto.getSvnUrl()));
			// outputDto
			// .setServerRoot(UrlUtil.getContext(inputDto.getSvnUrl()));
			// outputDto.setProtocol(RepoProtocol.valueOf(UrlUtil.getProtocol(
			// inputDto.getSvnUrl()).toUpperCase()));
			// }
			// if (StringUtils.isNotEmpty(inputDto.getViewvcUrl())) {
			// outputDto.setBrowsingServerIP(UrlUtil.getHost(inputDto
			// .getViewvcUrl()));
			// outputDto.setBrowsingServerRoot(UrlUtil.getContext(inputDto
			// .getViewvcUrl()));
			// }
			if (null != outputDto.getRepoType()) {
				outputDto.setRepoType(RepoType.SVN);
			}
			outputDto.setRepoRemoteId(inputDto.getId());

			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	public static ScmRepoInputParam Con2SCMRepo(GitRepoDTO inputDto) {
		ScmRepoInputParam outputDto = new ScmRepoInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setRepoName(inputDto.getName());
			}
			if (null != outputDto.getRepoType()) {
				outputDto.setRepoType(RepoType.GIT);
			}
			outputDto.setRepoRemoteId(inputDto.getId());
		}
		return outputDto;
	}

	public static ScmRepoInputParam Con2SCMRepo(SvnTemplateDTO inputDto,
			String serverIP) {
		ScmRepoInputParam outputDto = new ScmRepoInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setRepoName(inputDto.getName());
			}
			if (StringUtils.isNotEmpty(serverIP)) {
				outputDto.setServerIP(serverIP);
			}
			if (null != outputDto.getRepoType()) {
				outputDto.setRepoType(RepoType.SVN);
			}
			outputDto.setTemplateId(inputDto.getId());
//			outputDto.setProtocol(RepoProtocol.HTTP);
			outputDto.setRepoStyle(inputDto.getName());
			// outputDto.setRepoRemoteId(inputDto.getId());

			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}

	public static ScmRepoInputParam Con2SCMRepo(SvnTemplateDTO inputDto) {
		ScmRepoInputParam outputDto = new ScmRepoInputParam();
		if (inputDto != null) {
			if (StringUtils.isNotEmpty(inputDto.getName())) {
				outputDto.setRepoName(inputDto.getName());
			}
			if (null != outputDto.getRepoType()) {
				outputDto.setRepoType(RepoType.SVN);
			}
			outputDto.setTemplateId(inputDto.getId());
//			outputDto.setProtocol(RepoProtocol.HTTP);
			outputDto.setRepoStyle(inputDto.getName());
			// outputDto.setRepoRemoteId(inputDto.getId());

			// outputDto.setRemark(JSON.toJSONString(inputDto));
		}
		return outputDto;
	}
}
