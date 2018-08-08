package com.wanda.portal.dao.remote.impl;

import com.wanda.portal.dao.jpa.UserRepository;
import com.wanda.portal.dao.remote.UserService;
import com.wanda.portal.entity.Role;
import com.wanda.portal.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User findByUserKey(String userKey) {
        return userRepository.findByUserKey(userKey);
    }

    @Override
    public void save(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public Page<User> findAll(PageRequest of) {
        return userRepository.findAll(of);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<Role> getRoleByUserId(Long id) {
        return userRepository.findRoleByUserId(id);
    }
}
