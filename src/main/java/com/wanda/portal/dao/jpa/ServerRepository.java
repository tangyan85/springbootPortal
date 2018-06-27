package com.wanda.portal.dao.jpa;

import com.wanda.portal.constants.ServerType;
import com.wanda.portal.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {
    List<Server> findByServerType(ServerType type);
}
