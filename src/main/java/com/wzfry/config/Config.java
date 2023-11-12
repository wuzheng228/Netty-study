package com.wzfry.config;

import com.wzfry.protocol.Serllizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    static Properties properties;
    static {
        try (InputStream inputStream = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        String value = properties.getProperty("server.port");
        if (value == null) {
            return 8080;
        }
        return Integer.parseInt(value);
    }

    public static Serllizer.Algorithm getSerilizerAlgorithm() {
        String value = properties.getProperty("serilizer.algorithm");
        if (value == null) {
            return Serllizer.Algorithm.Java;
        }
        return Serllizer.Algorithm.valueOf(value);
    }
}
