package com.wanda.portal.dao.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.entity.Server;

public interface ServerRepository extends JpaRepository<Server, Long> {
    List<Server> findByServerType(ServerType type);
}
