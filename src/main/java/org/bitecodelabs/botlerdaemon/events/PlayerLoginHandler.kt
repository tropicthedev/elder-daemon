package org.bitecodelabs.botlerdaemon.events

import net.minecraft.server.MinecraftServer
import org.bitecodelabs.botlerdaemon.BotlerDaemon
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class PlayerLoginHandler : ServerPlayConnectionEvents.Join {
    override fun onPlayReady(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {
        BotlerDaemon.LOGGER.info(handler.getPlayer().uuidAsString + " Has joined the server")
    }
}