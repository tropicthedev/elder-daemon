package org.bitecodelabs.botlerdaemon.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.bitecodelabs.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.websocket.SocketClient;

public class ServerStartingHandler implements ServerLifecycleEvents.ServerStarting {
    @Override
    public void onServerStarting(MinecraftServer server) {
        SocketClient socketClient = SocketClient.getInstance();

        socketClient.emitEvent(String.valueOf(SocketClient.Events.BOTLER_SERVER_BOOT), "Server starting");

        BotlerDaemon.LOGGER.info("Botler Daemon is starting");
    }
}
