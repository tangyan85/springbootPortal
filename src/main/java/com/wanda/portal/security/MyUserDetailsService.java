package com.wanda.portal.security;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetails;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl;

public class MyUserDetailsService  extends CrowdUserDetailsServiceImpl{
	
	@Override
	public CrowdUserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException{
		
		CrowdUserDetails e = super.loadUserByUsername(username);
		System.out.println(e);
		System.out.println(e.getPassword());
		System.out.println(e.getRemotePrincipal());
		return e;
	}

}
