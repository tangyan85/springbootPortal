package com.wanda.portal.dao.jpa;

import com.wanda.portal.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

    @Modifying
    @Query("delete from UserRole ur where ur.userKey = :userKey")
    void deleteByUserKey(@Param("userKey") String userKey);

}
