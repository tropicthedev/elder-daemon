package org.bitecodelabs.botlerdaemon.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.bitecodelabs.botlerdaemon.Daemon;
import org.bitecodelabs.botlerdaemon.connections.SocketClient;

public class ServerStartHandler implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        SocketClient.getInstance(server);

        Daemon.LOGGER.info("Botler Daemon has started");
    }
}