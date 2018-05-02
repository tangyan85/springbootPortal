package com.wanda.portal.utils;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

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
}
