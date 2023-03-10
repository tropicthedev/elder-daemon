package com.github.tropicdev.elderdaemon.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    public static String API_TOKEN;
    public static String SERVER_ID;
    public static String HOST;

    static {
        Properties properties = new Properties();
        Properties newProperties = new Properties();
        Path path = FabricLoader.getInstance().getConfigDir().resolve("elder.properties");

        if (Files.isRegularFile(path)) {
            try {
                properties.load(Files.newInputStream(path, StandardOpenOption.CREATE));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        HOST = getString(properties, newProperties, "api_host", "ws://localhost:3000");
        API_TOKEN = getString(properties, newProperties, "api_token", "insert_api_token_here");
        SERVER_ID = getString(properties, newProperties, "server_id", "insert_server_id_here");

        try {
            newProperties.store(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING),
                    "Configuration file for Elder Daemon");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getString(Properties properties, Properties newProperties, String key, String def) {
        try {
            String property = properties.getProperty(key);
            newProperties.setProperty(key, property);
            return property;
        } catch (Exception e) {
            newProperties.setProperty(key, def);
            return def;
        }
    }
}
