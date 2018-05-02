package com.wanda.portal.utils;

import org.springframework.http.ResponseEntity;

public class RestLogUtils {

    private static final String HTTP_Return_Body = ", HTTP返回body为: ";
    private static final String HTTP_Return_Code = ", HTTP返回状态码为: ";

    public static <T> String packSuccHTTPLogs(String title, ResponseEntity<T> response) {
        return packHTTPLogs(title, "-返回HTTP成功", response);
    }

    public static <T> String packFailHTTPLogs(String title, ResponseEntity<T> response) {
        return packHTTPLogs(title, "-返回HTTP失败", response);
    }

    private static <T> String packHTTPLogs(String title, String res, ResponseEntity<T> response) {
        return title + res + HTTP_Return_Code + response.getStatusCode() + HTTP_Return_Body + response.getBody();
    }
}
