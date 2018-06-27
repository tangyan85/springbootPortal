package com.wanda.portal.component;

import com.wanda.portal.config.biz.LdapConfig;
import com.wanda.portal.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

/**
 * ldap认证组件
 *
 * @author cloud He
 * @since 1.0.0 2018/6/11
 */
@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	LdapConfig ldapConfig;

	private LdapContext ctx = null;
	private final Control[] controls = null;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		boolean result = verify(username, password);

		if (!result) {
			throw new BadCredentialsException("Wrong password.");
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);

		return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}

	private void getConnection() {
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapConfig.getUrl() + ldapConfig.getGroupSearchBase());
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ldapConfig.getManagerDn());
		env.put(Context.SECURITY_CREDENTIALS, ldapConfig.getManagerPassword());

		try {
			ctx = new InitialLdapContext(env, controls);
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	private void closeContext() {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

	private String getUserDn(String cn) {
		getConnection();
		StringBuilder userDN = new StringBuilder();
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> en = ctx.search("", "cn=" + cn, constraints);

			if (en == null || !en.hasMoreElements()) {
				throw new NullPointerException("search result is null!");
			}

			// maybe more than one element
			while (en.hasMoreElements()) {
				Object obj = en.nextElement();
				if (obj != null) {
					SearchResult si = (SearchResult) obj;
					userDN.append(si.getName());
					userDN.append(",").append(ldapConfig.getGroupSearchBase());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userDN.toString();
	}

	private boolean verify(String cn, String password) {
		boolean result = false;
		String userDN = getUserDn(cn);

		if (StringUtils.isNotEmpty(userDN)) {
			try {
				ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
				ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
				ctx.reconnect(controls);
				result = true;
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		closeContext();
		return result;
	}
}
