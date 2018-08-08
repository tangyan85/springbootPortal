package com.wanda.portal.facade;

import com.wanda.portal.config.biz.IdConfig;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class LdapIndexController {
	@Autowired
	ServerRepository serverRepository;
	
	@Autowired
	JiraService jiraService;

    @Autowired
    IdConfig idConfig;
    
//	@GetMapping(value = "/idx")
//	public String home(Model model, HttpSession session) {
//	    UserDetails user = (UserDetails) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
//	    logger.info("User is = " + user);
//	    logger.info("whoamI? " + idConfig.getWhoami());
//        return "idx";
//	}
 
//    @GetMapping("/loginLdap")
//    public String login(Model model, HttpSession session, @RequestParam(value = "error", required = false) String error) {
//        logger.info("entering into loginLdap");
//        if (error != null) {
//            model.addAttribute("error", "用户名或密码错误");
//        }
//        return "login_page";
//    }
    
//    @GetMapping("/logoutLdap")
//    public String logout(HttpSession session){
//        session.removeAttribute(SecurityConfigCrowd.SESSION_KEY);
//        return "redirect:/loginLdap";
//    }
    
}
