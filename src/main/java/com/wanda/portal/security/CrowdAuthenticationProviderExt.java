package com.wanda.portal.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.atlassian.crowd.exception.ApplicationAccessDeniedException;
import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.ExpiredCredentialException;
import com.atlassian.crowd.exception.InactiveAccountException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator;
import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService;
import com.atlassian.crowd.model.authentication.ValidationFactor;
import com.atlassian.crowd.service.client.CrowdClient;
import com.wanda.portal.constants.SM4;
import com.wanda.portal.utils.RedisUtil;
import com.wanda.portal.utils.sm4.SM4Crypter;
/**
 * 记录密码时候，对RemoteCrowdAuthenticationProvider不做任何修改
 * @author tangyan21
 *
 */
public class CrowdAuthenticationProviderExt extends
		RemoteCrowdAuthenticationProvider {
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
		System.out.println(username+" has stored in redis;");
		
		//String value = redisUtil.get(SM4.PASSWORD_PREFIX+username);
		//final String decodePsw = SM4Crypter.decrypt(value, SM4.SM4_KEY, SM4.SM4_IV);
		//System.out.println(username+":"+decodePsw);
		return super.authenticate(username, password, validationFactors);
	}
}