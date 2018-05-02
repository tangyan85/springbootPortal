package com.wanda.portal.dto.common;

import java.io.Serializable;

public class CommonHttpResponseBody implements Serializable {

    private static final long serialVersionUID = -3009254940891823438L;
    
    public static final String SUCCESS_CODE = "0000";
    public static final String SUCCESS_MSG = "success";
    
    public static final String FAIL_CODE = "5000";
    public static final String FAIL_MSG = "fail";
    
    public static final CommonHttpResponseBody packSuccess() {
        CommonHttpResponseBody ret = new CommonHttpResponseBody();
        ret.setResponseCode(SUCCESS_CODE);
        ret.setResponseMsg(SUCCESS_MSG);
        return ret;
    }
    
    public static final CommonHttpResponseBody packFailure() {
        CommonHttpResponseBody ret = new CommonHttpResponseBody();
        ret.setResponseCode(FAIL_CODE);
        ret.setResponseMsg(FAIL_MSG);
        return ret;
    }
    
    private String responseCode = SUCCESS_CODE;

    private String responseMsg = SUCCESS_MSG;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    @Override
    public String toString() {
        return "CommonHttpResponseBody [responseCode=" + responseCode + ", responseMsg=" + responseMsg + "]";
    }

}
