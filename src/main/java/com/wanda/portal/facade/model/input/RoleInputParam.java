package com.wanda.portal.facade.model.input;

import java.io.Serializable;

public class RoleInputParam implements Serializable {
    private String roleId;
    private String roleName;
    private String roleKey;
    private String isAdmin;
    private String enable;
    private Long[] menus;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public Long[] getMenus() {
        return menus;
    }

    public void setMenus(Long[] menus) {
        this.menus = menus;
    }
}
