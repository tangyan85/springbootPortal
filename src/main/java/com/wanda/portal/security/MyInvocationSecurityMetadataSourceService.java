package com.wanda.portal.security;

import com.wanda.portal.dao.remote.MenuService;
import com.wanda.portal.entity.Menu;
import com.wanda.portal.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private MenuService menuService;

    private Map<String, Collection<ConfigAttribute>> map = null;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        /*String url = ((FilterInvocation) o).getRequestUrl();
        if (url.startsWith("/login")
                || url.startsWith("/assets")
                || url.startsWith("/bower_components")
                || url.startsWith("/css")
                || url.startsWith("/data")
                || url.startsWith("/dist")
                || url.startsWith("/img")
                || url.startsWith("/js")
                || url.startsWith("/plugins")
                || url.startsWith("/vendor")
                || url.startsWith("brand.ico")
                || url.startsWith("/pages")
                || url.startsWith("/500")
                || url.startsWith("/404")
                || url.startsWith("/403")
                ) {
            return null;
        }*/
        loadResourceDefine();
        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
        for (String key : map.keySet()) {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(key);
            if (matcher.matches(request)) {
                return map.get(key);
            }
        }
        return null;
        /*return new ArrayList<ConfigAttribute>(1) {
            private static final long serialVersionUID = -4429808118051966907L;
            {
                add(new SecurityConfig("ROLE_$"));
            }
        };*/
    }

    private void loadResourceDefine() {
        map = new HashMap<>(10);
        List<Menu> menus = menuService.findAll();
        for (Menu menu : menus) {
            String key = menu.getPath();
            Collection<ConfigAttribute> ca = new ArrayList<>();
            for (Role role : menu.getRoles()) {
                ca.add(new SecurityConfig(role.getRoleKey()));
            }
            if (ca.size() <= 0) {
                ca.add(new SecurityConfig("ROLE_$"));
            }
            map.put(key, ca);
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
