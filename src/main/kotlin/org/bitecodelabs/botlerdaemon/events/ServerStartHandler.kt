package org.bitecodelabs.botlerdaemon.events

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import org.bitecodelabs.botlerdaemon.Daemon
import org.bitecodelabs.botlerdaemon.connections.SocketClient

class ServerStartHandler: ServerLifecycleEvents.ServerStarted {
    override fun onServerStarted(server: MinecraftServer) {

        SocketClient.getInstance(server)

        Daemon.LOGGER.info("Socket client initialized")

        Daemon.LOGGER.info("Botler Daemon is started")
    }

}