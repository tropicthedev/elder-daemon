package org.bitecodelabs.botlerdaemon.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    public static String BOTLER_API_TOKEN;
    public static String BOTLER_SERVER_ID;
    public static String BOTLER_GUILD_ID;
    public static String HOST;

    public enum SocketEvents {
        BOTLER_MEMBER_ADD("BOTLER_SERVER_MEMBER_ADD"),
        BOTLER_MEMBER_ADD_SUCCESS("BOTLER_MEMBER_ADD_SUCCESS"),
        BOTLER_MEMBER_REMOVE("BOTLER_SERVER_MEMBER_REMOVE"),
        BOTLER_MEMBER_REMOVE_SUCCESS("BOTLER_MEMBER_REMOVE_SUCCESS"),
        BOTLER_MEMBER_BAN("BOTLER_SERVER_MEMBER_BAN"),
        BOTLER_MEMBER_BAN_SUCCESS("BOTLER_SERVER_MEMBER_BAN_SUCCESS"),
        DAEMON_MEMBER_BAN("DAEMON_SERVER_MEMBER_BAN"),
        DAEMON_MEMBER_BAN_SUCCESS("DAEMON_SERVER_MEMBER_BAN");

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

        HOST = getString(properties, newProperties, "botler_host", "ws://localhost:3000");
        BOTLER_API_TOKEN = getString(properties, newProperties, "botler_api_token", "insert_api_token_here");
        BOTLER_SERVER_ID = getString(properties, newProperties, "botler_server_id", "insert_server_id_here");
        BOTLER_GUILD_ID = getString(properties, newProperties, "botler_guild_id", "insert_guild_id_here");

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
