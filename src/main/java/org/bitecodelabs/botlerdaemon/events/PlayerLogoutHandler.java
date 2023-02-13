package org.bitecodelabs.botlerdaemon.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.bitecodelabs.botlerdaemon.Daemon;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.connections.ApiClient;
import org.bitecodelabs.botlerdaemon.connections.Callback;

public class PlayerLogoutHandler implements ServerPlayConnectionEvents.Disconnect {

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ApiClient apiClient = new ApiClient();

        apiClient.patch("/" + Config.BOTLER_GUILD_ID + "/server/" + Config.BOTLER_SERVER_ID + "/session/" + handler.getPlayer().getUuidAsString(), new Callback());

        Daemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has left the server");
    }
}
