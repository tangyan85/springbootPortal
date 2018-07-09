package com.wanda.portal.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanda.portal.entity.User;

public interface UserRepository  extends JpaRepository<User, Long>{

    User findByUserkey(String userkey);
}
