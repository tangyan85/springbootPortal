package com.wanda.portal.exception;

public class CreateProjectValidationException extends Exception {

    private static final long serialVersionUID = -3345609875612528518L;

    public CreateProjectValidationException() {
        super("Create Project failed with validation");
    }

    public CreateProjectValidationException(String msg) {
        super(msg);
    }
    
}
