package com.wanda.portal.dao.remote;

import com.wanda.portal.dto.confluence.CreateConfluenceSpaceParamDTO;
import com.wanda.portal.dto.confluence.GenericConfluenceSpaceDTO;
import com.wanda.portal.entity.ConfluenceSpace;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;

import java.util.List;

public interface ConfluenceService {

    public String createSpace(CreateConfluenceSpaceParamDTO confluenceSpace, Server server) throws Exception;
    
    @Deprecated
    public ConfluenceSpace getConfluenceSpaceByKey(String spaceKey);

    public List<GenericConfluenceSpaceDTO> fetchAllConfluenceSpaces(Server server);

	List<ConfluenceSpaceInputParam> fetchUnusedConfs(Server server);

    void deleteByConfluenceId(Long confluenceId);

    String findSpace(CreateConfluenceSpaceParamDTO confluenceSpace, Server server) throws Exception;

    Integer fetchAllPages(final String projectId, Server server);

    Integer fetchAllPagesByCreated(final String projectId, Server server);

    Integer fetchAllPagesByModified(final String projectId, Server server);
}
