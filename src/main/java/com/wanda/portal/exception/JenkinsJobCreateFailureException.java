package com.wanda.portal.exception;

public class JenkinsJobCreateFailureException extends Exception {

    private static final long serialVersionUID = -2262186916144784567L;

    public JenkinsJobCreateFailureException() {
        super("Jenkins Job Failed to Create！");
    }

    public JenkinsJobCreateFailureException(String msg) {
        super(msg);
    }
}
