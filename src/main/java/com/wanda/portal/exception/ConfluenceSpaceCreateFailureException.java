package com.wanda.portal.exception;

public class ConfluenceSpaceCreateFailureException extends Exception {

    private static final long serialVersionUID = -2262186916144784567L;

    public ConfluenceSpaceCreateFailureException() {
        super("Confluence Space Failed to Create！");
    }

    public ConfluenceSpaceCreateFailureException(String msg) {
        super(msg);
    }
}
