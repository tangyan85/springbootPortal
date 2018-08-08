package com.wanda.portal.dao.jpa;

import com.wanda.portal.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long>{

    @Modifying
    @Query("delete from RoleMenu rm where rm.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
}
