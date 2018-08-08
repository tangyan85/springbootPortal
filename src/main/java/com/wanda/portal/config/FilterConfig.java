package com.wanda.portal.config;

import com.wanda.portal.component.AopFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 *
 * @author cloud He
 * @since 1.0.0 2018/7/16
 */
@Configuration
public class FilterConfig {
    @Bean
    public AopFilter aopFilter() {
        return new AopFilter();
    }

}
