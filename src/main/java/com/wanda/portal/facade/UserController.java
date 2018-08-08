package com.wanda.portal.facade;

import com.wanda.portal.dao.remote.RoleService;
import com.wanda.portal.dao.remote.UserRoleService;
import com.wanda.portal.dao.remote.UserService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Role;
import com.wanda.portal.entity.User;
import com.wanda.portal.facade.model.input.UserInputParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;

    @RequestMapping("/toList")
    public String toList(Model model, String userId) {
        logger.info("------Current path:/user/toList");
        model.addAttribute("users", userService.findAll(PageRequest.of(0, 10)));
        model.addAttribute("userId", userId);
        return "user/toList";
    }

    @ResponseBody
    @RequestMapping(value = "/page/{page}/{size}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<User> page(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        return userService.findAll(PageRequest.of(page - 1, size));
    }

    @RequestMapping(value = "/preview/{userId}")
    public String preview(Model model, @PathVariable("userId") String userId) {
        User user = new User();
        try {
            if (StringUtils.isNotEmpty(userId)) {
                Long id = Long.valueOf(userId);
                user = userService.getUserById(id);
                List<Role> roles = userService.getRoleByUserId(id);
                List<Role> allRole = roleService.findAll();
                for (Role role : allRole) {
                    if (roles.contains(role)) {
                        role.setSelected(true);
                    } else {
                        role.setSelected(false);
                    }
                }
                user.setRoles(allRole);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("user", user);

        return "user/toDetail";
    }

    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody addMenu(@RequestBody UserInputParam userInputParam,
                                       RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        try {
            userRoleService.resetRole(userInputParam);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }
}
