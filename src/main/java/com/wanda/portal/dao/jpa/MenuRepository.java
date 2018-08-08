package com.wanda.portal.dao.jpa;

import com.wanda.portal.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long>{

    @Query(value = "select m.id, m.menu_name, m.path, m.enable from menu m inner join role_menu rm on m.id = rm.menu_id inner join role r on rm.role_id = r.id where r.role_key = :roleKey", nativeQuery = true)
    List<Menu> findByRoleKey(@Param("roleKey") String roleKey);
}
