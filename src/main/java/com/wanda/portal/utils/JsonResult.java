package com.wanda.portal.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResult {
	private boolean success;
	private String code;
	private String msg;
	private Map<String, Object> result = new HashMap();
	private List<String> errlist = new ArrayList();

	public List<String> getErrlist() {
		return this.errlist;
	}

	public void setErrlist(List<String> errlist) {
		this.errlist = errlist;
	}

	public JsonResult(boolean success, String code, String msg, List<String> errlist) {
		this.success = success;
		this.code = code;
		this.msg = msg;
		this.errlist = errlist;
	}

	public JsonResult(boolean success, String code, String msg) {
		this.success = success;
		this.code = code;
		this.msg = msg;
	}

	public JsonResult(boolean success) {
		this.success = success;
	}

	public void addData(String key, Object data) {
		this.result.put(key, data);
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return this.code;
	}

	public JsonResult setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return this.msg;
	}

	public JsonResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Map<String, Object> getResult() {
		return this.result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
}