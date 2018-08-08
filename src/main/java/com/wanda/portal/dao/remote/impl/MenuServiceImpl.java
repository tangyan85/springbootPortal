package com.wanda.portal.dao.remote.impl;

import com.wanda.portal.dao.jpa.MenuRepository;
import com.wanda.portal.dao.remote.MenuService;
import com.wanda.portal.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuRepository menuRepository;
    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public List<Menu> findByRoleKey(String username) {
        return menuRepository.findByRoleKey(username);
    }
}
