package com.wanda.portal.dao.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * rest 服务
 *
 * @author cloud he
 * @since 1.0.0 2018/6/28
 */
public abstract class AbstractRestService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRestService.class);

    @Autowired
    RestTemplate restTemplate;

    /**
     * rest请求
     *
     * @param values   http head参数{@link HttpHeaders}
     * @param body     json格式的输入参数
     * @param url      rest api url
     * @param method   http method
     * @param function 映射处理函数
     * @param <R>      映射函数输出类型
     * @return response.getBody映射到自定义类的对象
     */
    protected <R> R restRequest(Map<String, String> values, String body, String url, HttpMethod method, Function<ResponseEntity<String>, R> function) {
        logger.info("restRequest -> " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

        for (String key : values.keySet()) {
            headers.add(key, values.get(key));
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(url, method, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return function.apply(response);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

}
