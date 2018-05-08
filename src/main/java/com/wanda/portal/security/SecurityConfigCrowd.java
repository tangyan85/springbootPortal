package com.wanda.portal.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

// import com.adfaspace.jay.app.Application;
// import com.adfaspace.jay.backend.data.LocalAuthority;
// import com.adfaspace.jay.backend.service.UserService;
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticatorImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelper;
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelperImpl;
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractor;
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractorImpl;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.integration.springsecurity.CrowdLogoutHandler;
import com.atlassian.crowd.integration.springsecurity.CrowdSSOAuthenticationProcessingFilter;
import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider;
import com.atlassian.crowd.integration.springsecurity.UsernameStoringAuthenticationFailureHandler;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl;
import com.atlassian.crowd.service.client.ClientPropertiesImpl;
import com.atlassian.crowd.service.client.ClientResourceLocator;
import com.atlassian.crowd.service.client.CrowdClient;
import com.wanda.portal.config.biz.LdapConfig;
import com.wanda.portal.config.biz.IdConfig;

@EnableWebSecurity
@Configuration
@AutoConfigureAfter({ LdapConfig.class, IdConfig.class })
public class SecurityConfigCrowd extends WebSecurityConfigurerAdapter {

	public final static String SESSION_KEY = "user";

	@Autowired
	LdapConfig ldapConfig;
	@Autowired
	IdConfig idConfig;
	@Autowired
	private UserDetailsService userService;
	// 本地测试，屏蔽sso的开关的
	private boolean IsTest = true;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (ldapConfig.isEnable()) { // 采用ldap
			auth.ldapAuthentication().userDnPatterns("uid={0},ou=People").groupSearchBase("ou=Group").contextSource()
					.url(ldapConfig.getUrl());
		} else if (IsTest) {
			auth.eraseCredentials(false).inMemoryAuthentication().passwordEncoder(new MyPasswordEncoder())
					.withUser("admin").password("123456").roles("ADMIN");
			;
		} else {
			auth.authenticationProvider(remoteCrowdAuthenticationProvider());
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (ldapConfig.isEnable()) { // 采用ldap
			http.csrf().disable().exceptionHandling().accessDeniedPage("/403").and().authorizeRequests()
					.antMatchers("/", "/login").permitAll()
					.antMatchers(new String[] { "/static/**", "/js/**", "/vendor/**", "/css/**", "/img/**",
							"/images/**", "/fonts/**", "/**/brand.ico",
							"/bower_components/**","/plugins/**","/dist/**","/data/**"})
					.permitAll().anyRequest().authenticated().and().headers().frameOptions().disable().and().formLogin()
					.loginPage("/login").successHandler(new AuthenticationSuccessHandler() {
						@Override
						public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
								Authentication arg2) throws IOException, ServletException {
							Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
							if (principal != null && principal instanceof UserDetails) {
								UserDetails user = (UserDetails) principal;
								// 维护在session中
								request.getSession().setAttribute(SESSION_KEY, user);
								response.sendRedirect("index"); // 通过了就将用户名记录session，并且重定向到idx
							}
						}
					}).permitAll().failureUrl("/loginError").and().logout().deleteCookies("JSESSIONID").permitAll();
		} else if (IsTest) {
			http.csrf().disable().exceptionHandling().accessDeniedPage("/403").and().authorizeRequests()
					.antMatchers("/", "/login").permitAll()
					.antMatchers(new String[] { "/static/**", "/js/**", "/vendor/**", "/css/**", "/img/**",
							"/images/**", "/fonts/**", "/**/brand.ico",
							"/bower_components/**","/plugins/**","/dist/**","/data/**"})
					.permitAll().anyRequest().permitAll().and().headers().frameOptions().disable().and().formLogin()
					.loginPage("/login").successForwardUrl("/index").permitAll().failureUrl("/loginError").and()
					.logout().logoutSuccessUrl("/logoutSuccess").permitAll();
		} else { // 不采用ldap
			// Not using Spring CSRF here to be able to use plain HTML
			http.csrf().disable().exceptionHandling().accessDeniedPage("/403").and().authorizeRequests()
					.antMatchers("/", "/login").permitAll()
					.antMatchers(new String[] { "/static/**", "/js/**", "/vendor/**", "/css/**", "/img/**",
							"/images/**", "/fonts/**", "/**/brand.ico",
							"/bower_components/**","/plugins/**","/dist/**","/data/**"})
					.permitAll()
					.anyRequest().authenticated()
					.and().headers().frameOptions().disable()
					.and().formLogin()
					.loginPage("/login").successForwardUrl("/index").permitAll().failureUrl("/loginError").and()
					.logout().deleteCookies("JSESSIONID").permitAll().and()
					.addFilter(crowdSSOAuthenticationProcessingFilter())
			;
		}
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public ClientResourceLocator resourceLocator() {
		return new ClientResourceLocator(fetchCrowdPropByEnv(idConfig.getWhoami()));
	}

	/*
	 * 根据环境名，选择相应的crowd配置文件
	 */
	private static String fetchCrowdPropByEnv(String env) {
		if ("prod".equals(env) || "uat".equals(env) || env.startsWith("dev")) {
			return "crowd-" + env + ".properties";
		}
		return "";
	}

	@Bean
	public ClientPropertiesImpl clientProperties() {
		return ClientPropertiesImpl.newInstanceFromResourceLocator(resourceLocator());
	}

	@Bean
	public RestCrowdClientFactory crowdClientFactory() {
		return new RestCrowdClientFactory();
	}

	@Bean
	public CrowdClient crowdClient() {
		return crowdClientFactory().newInstance(clientProperties());
	}

	@Bean
	public CrowdHttpValidationFactorExtractor validationFactorExtractor() {
		return CrowdHttpValidationFactorExtractorImpl.getInstance();
	}

	@Bean
	public CrowdHttpTokenHelper tokenHelper() {
		return CrowdHttpTokenHelperImpl.getInstance(validationFactorExtractor());
	}

	@Bean
	public CrowdHttpAuthenticatorImpl crowdHttpAuthenticator() {
		return new CrowdHttpAuthenticatorImpl(crowdClient(), clientProperties(), tokenHelper());
	}

	@Bean
	public CrowdUserDetailsServiceImpl crowdUserDetailsService() {
		CrowdUserDetailsServiceImpl cuds = new CrowdUserDetailsServiceImpl();
		cuds.setCrowdClient(crowdClient());
		return cuds;
	}

	@Bean
	public RemoteCrowdAuthenticationProvider remoteCrowdAuthenticationProvider() {
		return new CrowdAuthenticationProviderExt(crowdClient(), crowdHttpAuthenticator(), crowdUserDetailsService());
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setDefaultTargetUrl("/index");
		return successHandler;
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		UsernameStoringAuthenticationFailureHandler failureHandler = new UsernameStoringAuthenticationFailureHandler(
				"username");
		failureHandler.setDefaultFailureUrl("/loginError");
		return failureHandler;
	}

	@Bean
	public CrowdLogoutHandler crowdLogoutHandler() {
		CrowdLogoutHandler crowdLogoutHandler = new CrowdLogoutHandler();
		crowdLogoutHandler.setHttpAuthenticator(crowdHttpAuthenticator());
		return crowdLogoutHandler;
	}

	@Bean
	public LogoutFilter logoutFilter() {
		LogoutFilter filter = new LogoutFilter("/logout", crowdLogoutHandler(), new SecurityContextLogoutHandler());
		return filter;
	}

	/**
	 * 认证过滤器
	 */
	@Bean
	public CrowdSSOAuthenticationProcessingFilter crowdSSOAuthenticationProcessingFilter() throws Exception {
		CrowdSSOAuthenticationProcessingFilter filter = new CrowdSSOAuthenticationProcessingFilter(tokenHelper(),
				crowdClient(), clientProperties());
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
		filter.setAuthenticationFailureHandler(authenticationFailureHandler());
		return filter;
	}

	
	   @Override  
	    public void configure(WebSecurity web) throws Exception {  
	        web.ignoring().antMatchers("/static/**");  
	    }  

}
