package org.bitecodelabs.com.botlerdaemon.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.bitecodelabs.com.botlerdaemon.BotlerDaemon;

public class PlayerLogoutHandler implements ServerPlayConnectionEvents.Disconnect {
    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        BotlerDaemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has left the server");
    }
}
