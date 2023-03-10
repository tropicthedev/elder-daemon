package com.github.tropicdev.elderdaemon.events;

import com.github.tropicdev.elderdaemon.Daemon;
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

            socketClient.emitJoinEvent(handler.getPlayer().getUuidAsString());

            Daemon.LOGGER.info(handler.getPlayer().getName() + " has joined the server, starting session");

        } catch (Error e) {

            Daemon.LOGGER.error(e.getMessage());

        }

    }
}