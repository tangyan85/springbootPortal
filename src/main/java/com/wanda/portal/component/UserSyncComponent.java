package com.wanda.portal.component;

import com.wanda.portal.config.biz.LdapConfig;
import com.wanda.portal.dao.jpa.UserRepository;
import com.wanda.portal.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;
import java.util.List;

/**
 * 用户同步组件
 *
 * @author cloud He
 * @since 1.0.0 2018/7/9
 */
@Component
public class UserSyncComponent {
    private static final Logger log = LoggerFactory.getLogger(UserSyncComponent.class);

    @Autowired
    LdapConfig ldapConfig;
    @Autowired
    UserRepository userRepository;

    @Scheduled(initialDelay = 300000, fixedRate = 1800000)
    public void run() {
        log.info("开始用户数据同步......");

        LdapAuthentication ldap = new LdapAuthentication(ldapConfig.getUrl(),
                ldapConfig.getGroupSearchBase(), ldapConfig.getManagerDn(), ldapConfig.getManagerPassword());
        List<SearchResult> results = ldap.getAllUser();
        for (SearchResult si : results) {
            nonExistedAddUser(si);
        }
        log.info("用户数据同步结束......");
    }

    private void nonExistedAddUser(SearchResult si) {
        try {
            Attribute usernameAttr = si.getAttributes().get("cn");
            Attribute mobileAttr = si.getAttributes().get("mobile");
            Attribute mailAttr = si.getAttributes().get("mail");
            Attribute oAttr = si.getAttributes().get("o");
            Attribute snAttr = si.getAttributes().get("sn");

            User remoteUser = new User();
            remoteUser.setUserKey(snAttr != null ? usernameAttr.get().toString() : "");
            remoteUser.setUsername(snAttr != null ? snAttr.get().toString() : "");
            remoteUser.setMobile(mobileAttr != null ? mobileAttr.get().toString() : "");
            remoteUser.setMail(mailAttr != null ? mailAttr.get().toString() : "");
            remoteUser.setDept(oAttr != null ? oAttr.get().toString() : "");

            User localUser = userRepository.findByUserKey(remoteUser.getUserKey());
            if (localUser == null) {
                log.info("Save: " + remoteUser.toString());
                userRepository.saveAndFlush(remoteUser);
            } else if (!remoteUser.equals(localUser)) {
                localUser.setUsername(remoteUser.getUsername());
                localUser.setMobile(remoteUser.getMobile());
                localUser.setMail(remoteUser.getMail());
                localUser.setDept(remoteUser.getDept());
                log.info("Update: " + localUser.toString());
                userRepository.saveAndFlush(localUser);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
