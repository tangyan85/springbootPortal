package com.wanda.portal.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * 配置信息打印组件
 *
 * @author cloud He
 * @since 1.0.0 2018/5/11
 */
@Component
public class ConfigPrintComponent implements CommandLineRunner, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(ConfigPrintComponent.class);

    private ConfigurableApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");

        Iterator<PropertySource<?>> i = context.getEnvironment().getPropertySources().iterator();
        for (; i.hasNext(); ) {
            String name = i.next().getName();
            if (name.startsWith("applicationConfig")) {
                String fileName = name.substring(name.indexOf("/") + 1, name.length() - 1);
                printYaml(fileName);
            }
        }

        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            String crowdFileName = "crowd-" + profile + ".properties";
            printProperties(crowdFileName);
        }

        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * 打印yaml配置文件内容
     *
     * @param fileName 文件名
     */
    private static void printYaml(String fileName) {
        log.info("--------" + fileName + "--------");
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(fileName));
        Properties prop = yaml.getObject();
        doPrint(prop);
    }

    /**
     * 打印properties配置文件内容
     *
     * @param fileName 文件名
     */
    private static void printProperties(String fileName) {
        log.info("--------" + fileName + "--------");
        Resource resource = new ClassPathResource(fileName);
        Properties prop = new Properties();
        try {
            prop.load(resource.getInputStream());
            doPrint(prop);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 打印属性
     *
     * @param prop 属性
     */
    private static void doPrint(Properties prop) {
        Enumeration<?> en = prop.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String value = prop.getProperty(key);
            log.info(">> " + key + " - " + value);
        }
    }

}
