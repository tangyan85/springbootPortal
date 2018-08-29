package com.wanda.portal.facade;

import com.wanda.portal.dao.remote.MenuService;
import com.wanda.portal.dao.remote.RoleMenuService;
import com.wanda.portal.dao.remote.RoleService;
import com.wanda.portal.dao.remote.UserRoleService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Menu;
import com.wanda.portal.entity.Role;
import com.wanda.portal.facade.model.input.RoleInputParam;
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

@RequestMapping("/role")
@Controller
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    RoleService roleService;

    @Autowired
    MenuService menuService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleMenuService roleMenuService;

    @RequestMapping("/toAdd")
    public String toAdd() {
        logger.info("------Current path:/role/toAdd");
        return "role/toAdd";
    }

    @RequestMapping("/toList")
    public String toList(Model model, String roleId) {
        logger.info("------Current path:/role/toList");
        model.addAttribute("roleId", roleId);
        return "role/toList";
    }

    @ResponseBody
    @RequestMapping(value = "/page/{page}/{size}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<Role> page(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        return roleService.findAll(PageRequest.of(page - 1, size));
    }

    @RequestMapping(value = "/preview/{roleId}")
    public String preview(Model model, @PathVariable("roleId") String roleId) {
        Role role = new Role();
        try {
            if (StringUtils.isNotEmpty(roleId)) {
                Long id = Long.valueOf(roleId);
                role = roleService.getRoleById(id);
                List<Menu> menus = roleService.getMenuByRoleId(id);
                List<Menu> allMenu = menuService.findAll();
                for (Menu menu : allMenu) {
                    if (menus.contains(menu)) {
                        menu.setSelected(true);
                    } else {
                        menu.setSelected(false);
                    }
                }
                role.setMenus(allMenu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("role", role);

        return "role/toDetail";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody save(@RequestBody RoleInputParam roleInputParam,
                                       RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        try {
            roleService.createRole(roleInputParam);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }

    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody addMenu(@RequestBody RoleInputParam roleInputParam,
                                       RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        try {
            roleMenuService.resetMenu(roleInputParam);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return response;
    }
}
