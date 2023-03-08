package com.github.tropicdev.elderdaemon.events;

import com.github.tropicdev.elderdaemon.Daemon;
import com.github.tropicdev.elderdaemon.connections.SocketClient;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStartHandler implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        SocketClient.getInstance(server);

        Daemon.LOGGER.info("Botler Daemon has started");
    }
}