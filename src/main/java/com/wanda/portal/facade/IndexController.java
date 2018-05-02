package com.wanda.portal.facade;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wanda.portal.config.biz.LdapConfig;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Server;
import com.wanda.portal.security.SecurityConfigCrowd;
import com.wanda.portal.utils.ConversionUtil;

@Controller
// @RequestMapping("/info")
public class IndexController {
	@Autowired
	ServerRepository serverRepository;
	@Autowired
	JiraService jiraService;
    @Autowired
    LdapConfig ldapConfig;
	
    @Autowired
    private StringRedisTemplate template;

	@RequestMapping(value = "/")
	public String home(Model model) {
		return "login";
	}

	@RequestMapping(value = "/login")
	public String loginPage(Model model) {
		System.out.println("------Current path:/login");
		return "login";
	}

	@RequestMapping(value = "/index")
	public String index(Model model, HttpSession session) {
        List<Server> list = new ArrayList<>();
        UserDetails ud = null;
        if (ldapConfig.isEnable()) {
            ud = (UserDetails) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
        } else {
            ud = getUserDetails();
        }
		model.addAttribute("currentUser", ud);
		list = serverRepository.findAll();
		for (Server s : list) {
			model.addAttribute(s.getServerType().toString() + "_SERVER",
					ConversionUtil.Con2ServerOutputParam(s));
		}

		return "index";
	}

	@RequestMapping("pages/{url}")
	public String redirect(@PathVariable("url") String url) {
		System.out.println("------Current path:/pages/" + url);
		return "pages/" + url;
	}

	@RequestMapping("/toLogin")
	@ResponseBody
	public CommonHttpResponseBody toLogin(Model model, @RequestBody User User) {
		System.out.println("------Current path:/toLogin/" + User);
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
		model.addAttribute("errorMsg", "登陆失败，用户名或者密码错误！");
		return "login";
	}
	
	@RequestMapping(value = "/logout",method = RequestMethod.GET)
	public String logoutPage (HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        if (ldapConfig.isEnable()) {
            session.removeAttribute(SecurityConfigCrowd.SESSION_KEY);
            return "redirect:/login";
        }
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("------Current path:/logout/");
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "redirect:/login";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}

	private UserDetails getUserDetails(){
		UserDetails ud=null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			ud=(UserDetails)principal;
		} 
		return ud;
	}
}
