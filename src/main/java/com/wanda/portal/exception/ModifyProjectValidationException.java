package com.wanda.portal.exception;

public class ModifyProjectValidationException extends Exception {

    private static final long serialVersionUID = -3245555929421833980L;

    public ModifyProjectValidationException() {
        super("Modify Project failed with validation");
    }

    public ModifyProjectValidationException(String msg) {
        super(msg);
    }
    
}
