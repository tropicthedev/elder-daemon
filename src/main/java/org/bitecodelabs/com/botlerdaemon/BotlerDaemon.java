package org.bitecodelabs.com.botlerdaemon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.bitecodelabs.com.botlerdaemon.event.PlayerLoginHandler;
import org.bitecodelabs.com.botlerdaemon.event.PlayerLogoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class BotlerDaemon implements ModInitializer {

    public static final String MOD_ID = "Botler Daemon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        System.out.println("Hello");

        File configFile = new File("config/config.json");

        ServerPlayConnectionEvents.JOIN.register((new PlayerLoginHandler()));
        ServerPlayConnectionEvents.DISCONNECT.register((new PlayerLogoutHandler()));

    }
}
