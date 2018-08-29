package com.wanda.portal.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanda.portal.component.LoginModeSetter;
import com.wanda.portal.config.biz.LdapConfig;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.*;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.dto.confluence.GenericConfluenceSpaceDTO;
import com.wanda.portal.dto.jira.GenericJiraProjectDTO;
import com.wanda.portal.entity.Server;
import com.wanda.portal.entity.User;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import com.wanda.portal.security.SecurityConfigCrowd;
import com.wanda.portal.utils.ConversionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    JiraService jiraService;
    @Autowired
    LdapConfig ldapConfig;
    @Autowired
    MenuService menuService;
    @Autowired
    ProjectService projectService;
    @Autowired
    ConfluenceService confluenceService;
    @Autowired
    RepoService repoService;

    @Autowired
    private StringRedisTemplate template;

    @RequestMapping(value = {"/login", "/"})
    public String loginPage(Model model) {
        logger.info("------Current path:/login");
        List<Server> servers = serverRepository.findAll();
        Map<String, List<Server>> map = new HashMap<>(3);
        for (ServerType serverType : ServerType.values()) {
            List<Server> list = new ArrayList<>();
            for (Server server : servers) {
                if (serverType.equals(server.getServerType())) {
                    list.add(server);
                }
            }
            map.put(serverType.name(), list);
        }
        model.addAttribute("servers", map);
        for (Server server : servers) {
            model.addAttribute(server.getServerType().toString() + "_SERVER",
                    ConversionUtil.Con2ServerOutputParam(server));
        }

        Map<String, Integer> aggregateMap = new HashMap<>(2);
        List<Object> aggregate = projectService.aggregatePoject();
        for (Object obj : aggregate) {
            Object[] list = (Object[]) obj;
            Integer count = Integer.parseInt(list[1].toString());
            aggregateMap.put(list[0].toString(), count);
        }
        if (aggregateMap.get("START") == null) {
            aggregateMap.put("START", 0);
        }
        if (aggregateMap.get("END") == null) {
            aggregateMap.put("END", 0);
        }
        model.addAttribute("aggregate", aggregateMap);

        Integer confTotal = 0;
        List<Server> confServer = serverRepository.findByServerType(ServerType.CONFLUENCE);
        for (Server server : confServer) {
            List<GenericConfluenceSpaceDTO> allConfs = confluenceService.fetchAllConfluenceSpaces(server);
            confTotal += allConfs.size();
        }
        model.addAttribute("confTotal", confTotal);

        Integer svnTotal = 0;
        List<Server> svnServer = serverRepository.findByServerType(ServerType.SVN);
        for (Server server : svnServer) {
            List<ScmRepoInputParam> allSvns = repoService.fetchAllScmByRepoType(server, RepoType.SVN);
            svnTotal += allSvns.size();
        }
        model.addAttribute("svnTotal", svnTotal);

        Integer gitTotal = 0;
        List<Server> gitServer = serverRepository.findByServerType(ServerType.GIT);
        for (Server server : gitServer) {
            List<ScmRepoInputParam> allGits = repoService.fetchAllScmByRepoType(server, RepoType.GIT);
            gitTotal += allGits.size();
        }
        model.addAttribute("gitTotal", gitTotal);

        Integer jiraTotal = 0;
        List<Server> jiraServer = serverRepository.findByServerType(ServerType.JIRA);
        for (Server server : jiraServer) {
            List<GenericJiraProjectDTO> allJiras = jiraService.fetchAllJiraProjects(server);
            jiraTotal += allJiras.size();
        }
        model.addAttribute("jiraTotal", jiraTotal);
        return "login";
    }

    @RequestMapping(value = "/index")
    public String index(Model model, HttpSession session) {
        User ud;
        if (ldapConfig.isEnable()) {
            ud = (User) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
        } else {
            ud = getUserDetails();
        }
        model.addAttribute("currentUser", ud);
        List<Server> servers = serverRepository.findAll();
        Map<String, List<Server>> map = new HashMap<>(3);
        for (ServerType serverType : ServerType.values()) {
            List<Server> list = new ArrayList<>();
            for (Server server : servers) {
                if (serverType.equals(server.getServerType())) {
                    list.add(server);
                }
            }
            map.put(serverType.name(), list);
        }
        model.addAttribute("servers", map);
        for (Server server : servers) {
            model.addAttribute(server.getServerType().toString() + "_SERVER",
                    ConversionUtil.Con2ServerOutputParam(server));
        }

        return "index";
    }

    @RequestMapping("/shouye")
    public String shouye(Model model, HttpSession session) {
        JSONArray todos = new JSONArray();
        JSONArray dones = new JSONArray();
        List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
        for (Server s : jiraServers) {
            s.setLoginMode(LoginModeSetter.LOGIN_MODE.CURR_USER.getModeCode());
            JSONArray todo = jiraService.fetchAllToDos(s);
            JSONArray done = jiraService.fetchAllDones(s);
            todos.addAll(todo);
            dones.addAll(done);
        }
        model.addAttribute("todos", limit(todos, 5));
        model.addAttribute("dones", limit(dones, 5));
        Server firstServer = jiraServers.get(0);
        User user = (User) session.getAttribute("user");
        String moreTodos = firstServer.getProtocol() + "://" + firstServer.getDomain()
                + "/issues/?jql=resolution %3D Unresolved AND assignee in (\\'"
                + user.getUserKey()
                + "\\') ORDER BY priority DESC,updated DESC";
        String moreDones = firstServer.getProtocol() + "://" + firstServer.getDomain()
                + "/issues/?jql=resolution !%3D Unresolved AND assignee in (\\'"
                + user.getUserKey()
                + "\\') AND status in (Closed, Done, 遗留, 已拒绝) ORDER BY priority DESC,updated DESC";
        model.addAttribute("moreTodos", moreTodos);
        model.addAttribute("moreDones", moreDones);
        return "pages/shouye";
    }

    @RequestMapping("pages/{url}")
    public String redirect(@PathVariable("url") String url) {
        logger.info("------Current path:/pages/" + url);
        return "pages/" + url;
    }

    @RequestMapping("/toLogin")
    @ResponseBody
    public CommonHttpResponseBody toLogin(Model model, @RequestBody User User) {
        logger.info("------Current path:/toLogin/" + User);
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();

        try {

        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }

        return response;
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    @RequestMapping("/loginError")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("loginMsg", "登陆失败，用户名或者密码错误！");
        return "login";
    }

    @RequestMapping("/logoutSuccess")
    public String logoutSuccess(Model model) {
        model.addAttribute("successLogout", true);
        model.addAttribute("loginMsg", "您已注销成功！");
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        if (ldapConfig.isEnable()) {
            session.removeAttribute(SecurityConfigCrowd.SESSION_KEY);
            return "redirect:/login";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("------Current path:/logout/");
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

    private User getUserDetails() {
        User ud = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            ud = (User) principal;
        }
        return ud;
    }

    private JSONArray limit(JSONArray jsonArray, int size) {
        List<JSONObject> list = jsonArray.toJavaList(JSONObject.class).stream().limit(size).collect(Collectors.toList());
        return JSONArray.parseArray(JSON.toJSONString(list));
    }
}
