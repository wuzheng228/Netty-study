package com.wzfry.server.service;

import com.wzfry.config.Config;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServicesFactory {
    static Properties properties;
    static Map<Class<?>, Object> map = new ConcurrentHashMap<>();// bean 管理容器

    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
            Set<String> names = properties.stringPropertyNames();
            for (String name : names) {
                if (name.endsWith("Service")) {
                    Class<?> interfaceClazz = Class.forName(name);
                    Class<?> instanceClazz = Class.forName(properties.getProperty(name));
                    map.put(interfaceClazz, instanceClazz.newInstance());
                }
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError();
        }
    }

    public static <T> T getServices(Class<T> interfaceClazz) {
        return (T) map.get(interfaceClazz);
    }
}
