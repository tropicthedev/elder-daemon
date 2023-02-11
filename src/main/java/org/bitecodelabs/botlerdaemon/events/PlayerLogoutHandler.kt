package org.bitecodelabs.botlerdaemon.events

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import org.bitecodelabs.botlerdaemon.BotlerDaemon

class PlayerLogoutHandler : ServerPlayConnectionEvents.Disconnect {
    override fun onPlayDisconnect(handler: ServerPlayNetworkHandler, server: MinecraftServer) {

        BotlerDaemon.LOGGER.info(handler.getPlayer().uuidAsString + " Has left the server")
    }
}