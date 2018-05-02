package com.wanda.portal.facade.model.input;

public interface InputParam {
    InputParam validateCreate() throws Exception;
    InputParam validateModify() throws Exception;
}
