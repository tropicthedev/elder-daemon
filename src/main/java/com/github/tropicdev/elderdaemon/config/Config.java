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

    public enum SocketEvents {
        GUARDIAN_MEMBER_BAN("GUARDIAN:MEMBER_BAN"),
        GUARDIAN_MEMBER_LEAVE("GUARDIAN:MEMBER_LEAVE"),
        GUARDIAN_MEMBER_ADD("GUARDIAN:MEMBER_ADD"),
        ELDER_MEMBER_BAN("ELDER:MEMBER_BAN"),
        ELDER_MEMBER_SESSION_START("ELDER:MEMBER_SESSION_START"),
        ELDER_MEMBER_SESSION_END("ELDER:MEMBER_SESSION_END"),
        SUCCESS("SUCCESS");

        private final String event;

        SocketEvents(String event) {
            this.event = event;
        }

        public String getEvent() {
            return event;
        }
    }

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

        HOST = getString(properties, newProperties, "api_host", "ws://localhost:3000");
        API_TOKEN = getString(properties, newProperties, "api_token", "insert_api_token_here");
        SERVER_ID = getString(properties, newProperties, "server_id", "insert_server_id_here");

        try {
            newProperties.store(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING),
                    "Configuration file for Botler Daemon");
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
