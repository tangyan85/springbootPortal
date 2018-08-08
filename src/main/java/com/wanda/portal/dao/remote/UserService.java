package com.wanda.portal.dao.remote;

import com.wanda.portal.entity.Role;
import com.wanda.portal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {
    User findByUserKey(String userKey);

    void save(User user);

    Page<User> findAll(PageRequest of);

    User getUserById(Long id);

    List<Role> getRoleByUserId(Long id);
}
