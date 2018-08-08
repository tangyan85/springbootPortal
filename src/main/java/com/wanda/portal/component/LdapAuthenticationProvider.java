package com.wanda.portal.component;

import com.wanda.portal.config.biz.LdapConfig;
import com.wanda.portal.dao.remote.UserService;
import com.wanda.portal.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;

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
    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userKey = authentication.getName();
        String password = (String) authentication.getCredentials();
        LdapAuthentication ldap = new LdapAuthentication(ldapConfig.getUrl(),
                ldapConfig.getGroupSearchBase(), ldapConfig.getManagerDn(), ldapConfig.getManagerPassword());
        boolean result = ldap.verify(userKey, password);

        if (!result) {
            throw new BadCredentialsException("Wrong password.");
        }

        User user = userService.findByUserKey(userKey);
        if (user == null) {
            SearchResult si = ldap.getUser(userKey);
            Attribute usernameAttr = si.getAttributes().get("cn");
            Attribute mobileAttr = si.getAttributes().get("mobile");
            Attribute mailAttr = si.getAttributes().get("mail");
            Attribute oAttr = si.getAttributes().get("o");
            Attribute snAttr = si.getAttributes().get("sn");

            user = new User();
            try {
                user.setUserKey(snAttr != null ? usernameAttr.get().toString() : "");
                user.setUsername(snAttr != null ? snAttr.get().toString() : "");
                user.setMobile(mobileAttr != null ? mobileAttr.get().toString() : "");
                user.setMail(mailAttr != null ? mailAttr.get().toString() : "");
                user.setDept(oAttr != null ? oAttr.get().toString() : "");
            } catch (NamingException e) {
                e.printStackTrace();
            }
            userService.save(user);
        }
        user.setPassword(password);
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
