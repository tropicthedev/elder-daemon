package org.bitecodelabs.botlerdaemon.events

import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import org.bitecodelabs.botlerdaemon.Daemon.Companion.LOGGER
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import org.bitecodelabs.botlerdaemon.config.Config
import org.bitecodelabs.botlerdaemon.connections.ApiClient
import org.bitecodelabs.botlerdaemon.connections.Callback

class PlayerLogoutHandler : ServerPlayConnectionEvents.Disconnect {
    //TODO Create an API caller to end sessions
    override fun onPlayDisconnect(handler: ServerPlayNetworkHandler, server: MinecraftServer) {

        val apiClient = ApiClient()

        apiClient.patch("/${Config.BOTLER_GUILD_ID}/server/${Config.BOTLER_SERVER_ID}/session/${handler.getPlayer().uuidAsString}", Callback())
        LOGGER.info(handler.getPlayer().uuidAsString + " Has left the server")
    }
}