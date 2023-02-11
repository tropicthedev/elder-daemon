package org.bitecodelabs.botlerdaemon.events

import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import org.bitecodelabs.botlerdaemon.Daemon.Companion.LOGGER
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class PlayerLogoutHandler : ServerPlayConnectionEvents.Disconnect {
    //TODO Create an API caller to end sessions
    override fun onPlayDisconnect(handler: ServerPlayNetworkHandler, server: MinecraftServer) {

        LOGGER.info(handler.getPlayer().uuidAsString + " Has left the server")
    }
}