package com.wanda.portal.security;

import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetails;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService extends CrowdUserDetailsServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
	
	@Override
	public CrowdUserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException{

		CrowdUserDetails e = super.loadUserByUsername(username);
		logger.info(e.toString());
		logger.info(e.getPassword());
		logger.info(e.getRemotePrincipal().toString());
		return e;
	}

}
