package org.bitecodelabs.botlerdaemon.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.bitecodelabs.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.websocket.SocketClient;

public class ServerStartHandler implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        SocketClient socketClient = SocketClient.getInstance();

        socketClient.emitEvent(String.valueOf(SocketClient.Events.BOTLER_SERVER_START), "Server started");

        BotlerDaemon.LOGGER.info("Botler Daemon is Up");
    }


}

