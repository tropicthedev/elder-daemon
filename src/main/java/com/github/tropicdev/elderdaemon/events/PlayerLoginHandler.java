package com.github.tropicdev.elderdaemon.events;

import com.github.tropicdev.elderdaemon.Daemon;
import com.github.tropicdev.elderdaemon.config.Config;
import com.github.tropicdev.elderdaemon.connections.SocketClient;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class PlayerLoginHandler implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

        SocketClient socketClient = SocketClient.getInstance(server);

        try {

            socketClient.emitJoinEvent(String.valueOf(Config.SocketEvents.ELDER_MEMBER_SESSION_START), handler.getPlayer().getUuidAsString());

            Daemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has joined the server");

        } catch (Error e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }
}