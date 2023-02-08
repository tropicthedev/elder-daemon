package org.bitecodelabs.com.botlerdaemon.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.bitecodelabs.com.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.com.botlerdaemon.websocket.SocketClient;

public class PlayerLoginHandler implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

        SocketClient socketClient = SocketClient.getInstance();

        socketClient.emitEvent(String.valueOf(SocketClient.Events.BOTLER_SERVER_MEMBER_JOIN), handler.getPlayer().getUuidAsString());

        BotlerDaemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has joined the server");

    }
}


