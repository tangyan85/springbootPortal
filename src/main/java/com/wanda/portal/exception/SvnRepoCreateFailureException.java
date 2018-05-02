package com.wanda.portal.exception;

public class SvnRepoCreateFailureException extends Exception {

	private static final long serialVersionUID = 6095136727983111947L;
	
	public SvnRepoCreateFailureException() {
		super("SVN REPO Failed to Create！");
	}
	public SvnRepoCreateFailureException(String msg) {
		super(msg);
	}
}
