package org.bitecodelabs.com.botlerdaemon.config;

// Adapted from https://github.com/RelativityMC/VMP-fabric

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

//TODO: Create Init Method To Check For Proper Credentials ??
public class Config {

    public static final String BOTLER_WEBSOCKET_HOST;
    public static final String BOTLER_API_TOKEN;

    static {
        final Properties properties = new Properties();
        final Properties newProperties = new Properties();
        final Path path = FabricLoader.getInstance().getConfigDir().resolve("botler.properties");
        if (Files.isRegularFile(path)) {
            try (InputStream in = Files.newInputStream(path, StandardOpenOption.CREATE)) {
                properties.load(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        BOTLER_WEBSOCKET_HOST = getString(properties, newProperties, "botler_websocket_host", "wss://localhost:3000");
        BOTLER_API_TOKEN = getString(properties, newProperties, "botler_api_token", "btler_falskdfjasdfja");

        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            newProperties.store(out, "Configuration file for Botler Daemon");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    def = Default
    private static String getString(Properties properties, Properties newProperties, String key, String def) {
        try {
            final String property = properties.getProperty(key);
            newProperties.setProperty(key, property);
            return property;
        } catch (Exception e) {
            newProperties.setProperty(key, String.valueOf(def));
            return def;
        }
    }

}
