package com.wanda.portal.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RestUtils {
    public static HttpHeaders packBasicAuthHeader(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        try {
            String plainCreds = username + ":" + password;
            byte[] plainCredsBytes = plainCreds.getBytes("utf-8");
            byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
            String base64Creds = new String(base64CredsBytes, "utf-8");
            headers.add("Authorization", "Basic " + base64Creds);
        } catch (UnsupportedEncodingException e) {
        }
        return headers;
    }

    public static Map<String, String> basicAuthHeader(String username, String password) {
        Map<String, String> headers = new HashMap<>(1);
        try {
            String plainCreds = username + ":" + password;
            byte[] plainCredsBytes = plainCreds.getBytes("utf-8");
            byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
            String base64Creds = new String(base64CredsBytes, "utf-8");
            headers.put("Authorization", "Basic " + base64Creds);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return headers;
    }

}
