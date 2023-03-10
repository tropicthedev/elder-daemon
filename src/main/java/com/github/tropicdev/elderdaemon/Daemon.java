package com.github.tropicdev.elderdaemon;

import com.github.tropicdev.elderdaemon.events.ServerEndHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.github.tropicdev.elderdaemon.events.PlayerLoginHandler;
import com.github.tropicdev.elderdaemon.events.PlayerLogoutHandler;
import com.github.tropicdev.elderdaemon.events.ServerStartHandler;

public class Daemon implements DedicatedServerModInitializer  {
    private static final String MOD_ID = "Elder Daemon";

    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {

        LOGGER.info("Starting Elder Daemon....");

        ServerPlayConnectionEvents.JOIN.register(new PlayerLoginHandler());

        ServerPlayConnectionEvents.DISCONNECT.register(new PlayerLogoutHandler());

        ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartHandler());

        ServerLifecycleEvents.SERVER_STOPPED.register(new ServerEndHandler());

    }

}