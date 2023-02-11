package org.bitecodelabs.botlerdaemon

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import org.bitecodelabs.botlerdaemon.connections.SocketClient
import org.bitecodelabs.botlerdaemon.events.PlayerLoginHandler
import org.bitecodelabs.botlerdaemon.events.PlayerLogoutHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BotlerDaemon : DedicatedServerModInitializer {

    private val socketClient = SocketClient()
    override fun onInitializeServer() {
        LOGGER.info("Starting Botler Daemon")
        socketClient.setup()
        ServerPlayConnectionEvents.JOIN.register(PlayerLoginHandler())
        ServerPlayConnectionEvents.DISCONNECT.register(PlayerLogoutHandler())
    }

    companion object {
        private const val MOD_ID = "Botler Daemon"
        @JvmField
        var LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    }
}