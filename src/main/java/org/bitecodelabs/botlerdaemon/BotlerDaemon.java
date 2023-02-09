package org.bitecodelabs.botlerdaemon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.bitecodelabs.botlerdaemon.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotlerDaemon implements ModInitializer {

    public static final String MOD_ID = "Botler Daemon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {

        BotlerDaemon.LOGGER.info("Starting Botler Daemon");

        ServerPlayConnectionEvents.JOIN.register((new PlayerLoginHandler()));

        ServerPlayConnectionEvents.DISCONNECT.register((new PlayerLogoutHandler()));

        ServerLifecycleEvents.SERVER_STARTED.register((new ServerStartHandler()));

        ServerLifecycleEvents.SERVER_STARTING.register((new ServerStartingHandler()));

        ServerLifecycleEvents.SERVER_STOPPED.register((new ServerShutdownHandler()));


    }
}
