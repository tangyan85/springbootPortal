package com.wanda.portal.facade.model.input;

import com.wanda.portal.constants.InputActionType;

public class AbstractInputParam implements InputParam {

    private InputActionType inputActionType;     
    
    public InputActionType getInputActionType() {
        return inputActionType;
    }

    public void setInputActionType(InputActionType inputActionType) {
        this.inputActionType = inputActionType;
    }

    @Override
    public InputParam validateCreate() throws Exception {
        return this;
    }

    @Override
    public InputParam validateModify() throws Exception {
        return this;
    }

    public void validByCondition(boolean isCreate) throws Exception {
        if (isCreate) {
            validateCreate();
        } else {
            validateModify();
        }
    }

}
