package com.wanda.portal.dao.remote.impl;

import com.wanda.portal.dao.jpa.RoleMenuRepository;
import com.wanda.portal.dao.remote.RoleMenuService;
import com.wanda.portal.entity.RoleMenu;
import com.wanda.portal.facade.model.input.RoleInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
public class RoleMenuServiceImpl implements RoleMenuService {
    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetMenu(RoleInputParam roleInputParam) throws Exception {
        Long roleId = Long.valueOf(roleInputParam.getRoleId());
        roleMenuRepository.deleteByRoleId(roleId);
        for (Long menuId : roleInputParam.getMenus()) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuRepository.saveAndFlush(roleMenu);
        }
    }
}
