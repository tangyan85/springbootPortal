package com.wanda.portal.dao.remote;

import java.util.List;
import com.wanda.portal.dto.confluence.CreateConfluenceSpaceParamDTO;
import com.wanda.portal.dto.confluence.GenericConfluenceSpaceDTO;
import com.wanda.portal.entity.ConfluenceSpace;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;

public interface ConfluenceService {

    public String createSpace(CreateConfluenceSpaceParamDTO confluenceSpace) throws Exception;
    
    @Deprecated
    public ConfluenceSpace getConfluenceSpaceByKey(String spaceKey);

    public List<GenericConfluenceSpaceDTO> fetchAllConfluenceSpaces();

	List<ConfluenceSpaceInputParam> fetchUnusedConfs();

    public void setServer(Server server);

    public Server getServer();
}
