package org.bitecodelabs.botlerdaemon.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import org.bitecodelabs.botlerdaemon.Daemon;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.connections.ApiClient;
import org.bitecodelabs.botlerdaemon.connections.Callback;

public class PlayerLoginHandler implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        ApiClient apiClient = new ApiClient();

        apiClient.post("/" + Config.BOTLER_GUILD_ID + "/server/" + Config.BOTLER_SERVER_ID + "/session/" + handler.getPlayer().getUuidAsString(), new Callback());

        Daemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has joined the server");
    }
}