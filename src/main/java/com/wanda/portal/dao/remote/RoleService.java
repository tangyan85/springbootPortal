package com.wanda.portal.dao.remote;

import com.wanda.portal.entity.Menu;
import com.wanda.portal.entity.Role;
import com.wanda.portal.facade.model.input.RoleInputParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RoleService {

    void createRole(RoleInputParam roleInputParam);

    Role getRoleById(Long aLong);

    Page<Role> findAll(PageRequest of);

    List<Menu> getMenuByRoleId(Long id);

    List<Role> findAll();
}
