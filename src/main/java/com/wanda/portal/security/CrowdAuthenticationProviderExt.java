package com.wanda.portal.security;

import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService;
import com.atlassian.crowd.model.authentication.ValidationFactor;
import com.atlassian.crowd.service.client.CrowdClient;
import com.wanda.portal.constants.SM4;
import com.wanda.portal.utils.RedisUtil;
import com.wanda.portal.utils.sm4.SM4Crypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/**
 * 记录密码时候，对RemoteCrowdAuthenticationProvider不做任何修改
 * @author tangyan21
 *
 */
public class CrowdAuthenticationProviderExt extends
		RemoteCrowdAuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(CrowdAuthenticationProviderExt.class);

	@Autowired
	private RedisUtil redisUtil;

	public CrowdAuthenticationProviderExt(CrowdClient authenticationManager,
			CrowdHttpAuthenticator httpAuthenticator,
			CrowdUserDetailsService userDetailsService) {
		super(authenticationManager, httpAuthenticator, userDetailsService);
	}

	@Override
	public String authenticate(String username, String password,
			List<ValidationFactor> validationFactors)
			throws InactiveAccountException, ExpiredCredentialException,
			ApplicationPermissionException, InvalidAuthenticationException,
			OperationFailedException, ApplicationAccessDeniedException {
		final String EncodePsw = SM4Crypter.encrypt(password,SM4.SM4_KEY, SM4.SM4_IV);
		
		redisUtil.set(SM4.PASSWORD_PREFIX+username, EncodePsw);
		logger.info(username+" has stored in redis;");
		
		//String value = redisUtil.get(SM4.PASSWORD_PREFIX+username);
		//final String decodePsw = SM4Crypter.decrypt(value, SM4.SM4_KEY, SM4.SM4_IV);
		//logger.info(username+":"+decodePsw);
		return super.authenticate(username, password, validationFactors);
	}
}