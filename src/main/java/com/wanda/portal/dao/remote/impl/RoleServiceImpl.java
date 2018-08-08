package com.wanda.portal.dao.remote.impl;

import com.wanda.portal.dao.jpa.RoleRepository;
import com.wanda.portal.dao.remote.RoleService;
import com.wanda.portal.entity.Menu;
import com.wanda.portal.entity.Role;
import com.wanda.portal.facade.model.input.RoleInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public void createRole(RoleInputParam roleInputParam) {
        Role role = new Role();
        role.setRoleName(roleInputParam.getRoleName());
        role.setRoleKey(roleInputParam.getRoleKey());
        role.setIsAdmin(roleInputParam.getIsAdmin());
        role.setEnable(roleInputParam.getEnable());
        roleRepository.saveAndFlush(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Page<Role> findAll(PageRequest of) {
        return roleRepository.findAll(of);
    }

    @Override
    public List<Menu> getMenuByRoleId(Long id) {
        return roleRepository.findMenuByRoleId(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
