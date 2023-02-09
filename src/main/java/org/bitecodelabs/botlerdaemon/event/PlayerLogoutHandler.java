package org.bitecodelabs.botlerdaemon.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.bitecodelabs.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.botlerdaemon.websocket.SocketClient;

public class PlayerLogoutHandler implements ServerPlayConnectionEvents.Disconnect {
    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        SocketClient socketClient = SocketClient.getInstance();

        socketClient.emitEvent(String.valueOf(SocketClient.Events.BOTLER_SERVER_MEMBER_LEAVE), handler.getPlayer().getUuidAsString());

        BotlerDaemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has left the server");
    }
}
