package com.wanda.portal.component;

import com.wanda.portal.entity.Server;
import com.wanda.portal.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 设置服务器的登录方式
 *
 * @author cloud He
 * @since 1.0.0 2018/7/16
 */
@Aspect
@Component
public class LoginModeSetter {
    private static final Logger logger = LoggerFactory.getLogger(LoginModeSetter.class);

    private static final String[] ignore = {"fetchAllConfluenceSpaces", "fetchAllScmByRepoType",
            "fetchAllSvnRepos", "fetchAllGitRepos", "fetchAllJiraProjects"};

    @Around("execution(* com.wanda.portal.dao.remote.*.*(..)) ")
    public Object loginModeAdvice(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (Arrays.asList(ignore).contains(pjp.getSignature().getName())) {
            return pjp.proceed(args);
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Server) {
                Object userObject = AopServletContext.getRequest().getSession().getAttribute("user");
                if (userObject == null) {
                    throw new NullPointerException("Session expired, Please login again!");
                }
                User user = (User) userObject;
                Server server = (Server) args[i];
                setLoginMode(server, user);
                args[i] = server;
            }
        }
        return pjp.proceed(args);
    }

    /**
     * 设置登录方式
     *
     * @param server 服务器信息
     * @param user   当前登录用户
     */
    private void setLoginMode(Server server, User user) {
        if (LOGIN_MODE.CURR_USER.getModeCode().equals(server.getLoginMode())) {
            server.setLoginName(user.getUsername());
            server.setPasswd(user.getPassword());
        }
    }

    private enum LOGIN_MODE {
        CURR_USER("1"),
        DB_USER("2");
        private String modeCode;

        LOGIN_MODE(String modeCode) {
            this.modeCode = modeCode;
        }

        public String getModeCode() {
            return modeCode;
        }
    }
}
