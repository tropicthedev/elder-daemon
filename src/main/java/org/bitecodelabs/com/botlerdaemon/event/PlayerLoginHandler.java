package org.bitecodelabs.com.botlerdaemon.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import org.bitecodelabs.com.botlerdaemon.BotlerDaemon;

public class PlayerLoginHandler implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

        handler.player.networkHandler.disconnect(Text.of("Account is not linked... Please link in discord using this command linkfk;djfllkdjsfalikj"));

        BotlerDaemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has joined the server");

    }
}


