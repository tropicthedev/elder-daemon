package org.bitecodelabs.botlerdaemon.config;

import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class Config {
    public static final String BOTLER_API_TOKEN;
    public static final String BOTLER_WEBSOCKET_HOST;

    static {
        Properties properties = new Properties();
        Properties newProperties = new Properties();
        Path path = FabricLoader.getInstance().getConfigDir().resolve("botler.properties");

        if (Files.isRegularFile(path)) {
            try {
                properties.load(Files.newInputStream(path, StandardOpenOption.CREATE));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        BOTLER_WEBSOCKET_HOST = getString(properties, newProperties, "botler_websocket_host", "ws://localhost:3000");
        BOTLER_API_TOKEN = getString(properties, newProperties, "botler_api_token", "insert_api_token_here");

        try {
            newProperties.store(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING), "Configuration file for Botler Daemon");
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