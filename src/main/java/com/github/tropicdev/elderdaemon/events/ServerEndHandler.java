package com.github.tropicdev.elderdaemon.events;

import com.github.tropicdev.elderdaemon.Daemon;
import com.github.tropicdev.elderdaemon.connections.SocketClient;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerEndHandler implements ServerLifecycleEvents.ServerStopped {
    @Override
    public void onServerStopped(MinecraftServer server) {
        SocketClient socketClient = SocketClient.getInstance(server);

        socketClient.closeInstance();

        Daemon.LOGGER.info("Elder has been stopped");
    }
}