package com.wanda.portal.dao.jpa;

import com.wanda.portal.entity.Menu;
import com.wanda.portal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>{

    List<Role> findByRoleKey(String authority);

    @Query("select m from Menu m inner join RoleMenu rm on m.menuId = rm.menuId inner join Role r on rm.roleId = r.roleId where r.roleId = :roleId")
    List<Menu> findMenuByRoleId(@Param("roleId") Long roleId);
}
