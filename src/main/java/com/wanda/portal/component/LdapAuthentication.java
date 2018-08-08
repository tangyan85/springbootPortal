package com.wanda.portal.component;

import org.apache.commons.lang3.StringUtils;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * ldap认证工具
 *
 * @author cloud He
 * @since 1.0.0 2018/7/9
 */
public class LdapAuthentication {
    private static final String SIMPLE = "simple";
    private static final String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private final String URL;
    private final String BASE_DN;
    private final String MANAGER_DN;
    private final String MANAGER_PASSWORD;

    private LdapContext ctx = null;
    private Control[] controls = null;

    public LdapAuthentication(
            String url, String baseDn, String managerDn, String managerPassword) {
        this.URL = url;
        this.BASE_DN = baseDn;
        this.MANAGER_DN = managerDn;
        this.MANAGER_PASSWORD = managerPassword;
    }

    public SearchResult getUser(String userKey) {
        List<SearchResult> results = search(userKey);
        return results.size() > 0 ? results.get(0) : null;
    }

    public List<SearchResult> getAllUser() {
        return search("*");
    }

    public boolean verify(String cn, String password) {
        boolean result = false;
        SearchResult sr = getUser(cn);
        String userDN = sr.getName() + "," + BASE_DN;

        getConnection();

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

    private List<SearchResult> search(String cn) {
        getConnection();
        List<SearchResult> results = new ArrayList<>();
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> en = ctx.search("", "cn=" + cn, constraints);

            if (en == null || !en.hasMoreElements()) {
                throw new NullPointerException("search result is null!");
            }

            while (en.hasMoreElements()) {
                Object obj = en.nextElement();
                if (obj != null) {
                    SearchResult si = (SearchResult) obj;
                    results.add(si);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeContext();
        return results;
    }

    private void getConnection() {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
        env.put(Context.PROVIDER_URL, URL + BASE_DN);
        env.put(Context.SECURITY_AUTHENTICATION, SIMPLE);
        env.put(Context.SECURITY_PRINCIPAL, MANAGER_DN);
        env.put(Context.SECURITY_CREDENTIALS, MANAGER_PASSWORD);

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
}
