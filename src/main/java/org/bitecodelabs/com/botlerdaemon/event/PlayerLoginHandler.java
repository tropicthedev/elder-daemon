package org.bitecodelabs.com.botlerdaemon.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.bitecodelabs.com.botlerdaemon.BotlerDaemon;

public class PlayerLoginHandler implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

        BotlerDaemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has joined the server");

    }
}


