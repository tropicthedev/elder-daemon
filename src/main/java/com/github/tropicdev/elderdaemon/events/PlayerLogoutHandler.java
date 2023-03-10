package com.github.tropicdev.elderdaemon.events;

import com.github.tropicdev.elderdaemon.Daemon;
import com.github.tropicdev.elderdaemon.connections.SocketClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class PlayerLogoutHandler implements ServerPlayConnectionEvents.Disconnect {

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {

        SocketClient socketClient = SocketClient.getInstance(server);

        try {

            socketClient.emitLeaveEvent(handler.getPlayer().getUuidAsString());

            Daemon.LOGGER.info(handler.getPlayer().getName() + " has left the server, ending session");

        } catch (Error e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }
}
