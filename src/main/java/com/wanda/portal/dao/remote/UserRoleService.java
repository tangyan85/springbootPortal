package com.wanda.portal.dao.remote;

import com.wanda.portal.facade.model.input.UserInputParam;

public interface UserRoleService {
    void resetRole(UserInputParam userInputParam) throws Exception;
}
