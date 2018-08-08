package com.wanda.portal.dao.remote.impl;

import com.wanda.portal.dao.jpa.UserRoleRepository;
import com.wanda.portal.dao.remote.UserRoleService;
import com.wanda.portal.entity.UserRole;
import com.wanda.portal.facade.model.input.UserInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    UserRoleRepository userRoleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetRole(UserInputParam userInputParam) throws Exception {
        String userKey = userInputParam.getUserKey();
        userRoleRepository.deleteByUserKey(userKey);
        for (String roleKey : userInputParam.getRoles()) {
            UserRole userRole = new UserRole();
            userRole.setUserKey(userKey);
            userRole.setRoleKey(roleKey);
            userRoleRepository.saveAndFlush(userRole);
        }
    }
}
