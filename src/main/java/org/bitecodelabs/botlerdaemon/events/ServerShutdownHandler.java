package org.bitecodelabs.botlerdaemon.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.bitecodelabs.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.websocket.SocketClient;

public class ServerShutdownHandler implements ServerLifecycleEvents.ServerStopped {
    @Override
    public void onServerStopped(MinecraftServer server) {
        SocketClient socketClient = SocketClient.getInstance();

        socketClient.emitEvent(String.valueOf(SocketClient.Events.BOTLER_SERVER_SHUTDOWN), "Server shutdown");

    }
}
