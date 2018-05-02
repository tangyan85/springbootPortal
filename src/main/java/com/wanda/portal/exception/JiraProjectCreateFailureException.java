package com.wanda.portal.exception;

public class JiraProjectCreateFailureException extends Exception {

    private static final long serialVersionUID = -5964720606870141286L;

    public JiraProjectCreateFailureException() {
        super("Jira Project Failed to Create！");
    }

    public JiraProjectCreateFailureException(String msg) {
        super(msg);
    }
}
