package org.bitecodelabs.botlerdaemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.bitecodelabs.botlerdaemon.events.PlayerLoginHandler;
import org.bitecodelabs.botlerdaemon.events.PlayerLogoutHandler;
import org.bitecodelabs.botlerdaemon.events.ServerStartHandler;

public class Daemon implements DedicatedServerModInitializer  {
    private static final String MOD_ID = "Botler Daemon";

    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {

        LOGGER.info("Starting Botler Daemon....");

        ServerPlayConnectionEvents.JOIN.register(new PlayerLoginHandler());

        ServerPlayConnectionEvents.DISCONNECT.register(new PlayerLogoutHandler());

        ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartHandler());
    }

}