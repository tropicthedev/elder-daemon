package org.bitecodelabs.botlerdaemon.events

import net.minecraft.server.MinecraftServer
import net.fabricmc.fabric.api.networking.v1.PacketSender
import org.bitecodelabs.botlerdaemon.Daemon.Companion.LOGGER
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class PlayerLoginHandler : ServerPlayConnectionEvents.Join {

    //TODO Create an API caller to start sessions
    override fun onPlayReady(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {

        LOGGER.info(handler.getPlayer().uuidAsString + " Has joined the server")

    }
}