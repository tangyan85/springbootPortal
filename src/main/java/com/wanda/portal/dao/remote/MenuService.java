package com.wanda.portal.dao.remote;

import com.wanda.portal.entity.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> findAll();

    List<Menu> findByRoleKey(String authority);
}
