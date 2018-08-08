package com.wanda.portal.dao.jpa;

import com.wanda.portal.entity.Role;
import com.wanda.portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByUserKey(String userKey);

    List<User> findByUserKeyLike(String userKey);

    @Query("select r from Role r inner join UserRole ur on ur.roleKey = r.roleKey inner join User u on u.userKey = ur.userKey where u.id = :userId")
    List<Role> findRoleByUserId(@Param("userId") Long userId);
}
