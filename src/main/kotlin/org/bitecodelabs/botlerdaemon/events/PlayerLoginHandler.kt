package org.bitecodelabs.botlerdaemon.events

import net.minecraft.server.MinecraftServer
import net.fabricmc.fabric.api.networking.v1.PacketSender
import org.bitecodelabs.botlerdaemon.Daemon.Companion.LOGGER
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import org.bitecodelabs.botlerdaemon.config.Config
import org.bitecodelabs.botlerdaemon.connections.ApiClient
import org.bitecodelabs.botlerdaemon.connections.Callback

class PlayerLoginHandler : ServerPlayConnectionEvents.Join {

    //TODO Create an API caller to start sessions
    override fun onPlayReady(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {

        val apiClient = ApiClient()

        apiClient.post("/${Config.BOTLER_GUILD_ID}/server/${Config.BOTLER_SERVER_ID}/session/${handler.getPlayer().uuidAsString}", Callback())

        LOGGER.info(handler.getPlayer().uuidAsString + " Has joined the server")

    }
}