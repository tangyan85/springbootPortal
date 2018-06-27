package com.wanda.portal.dao.remote;

import com.wanda.portal.dto.jenkins.JenkinsJobDTO;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.JenkinsInputParam;

import java.util.List;

public interface JenkinsService {

    /*获得所有Jenkins job*/
	List<JenkinsJobDTO> fetchAllJenkinsJobs();
	
	/*根据已有job创建新的job*/
	void createJenkinsUsingCopy(String newName, String fromName) throws Exception;

	List<JenkinsInputParam> fetchUnusedJekins();

    void setServer(Server server);
    
    Server getServer();

    void deleteByJenkinsId(Long jenkinsId);
}
