package org.bitecodelabs.botlerdaemon

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import net.fabricmc.api.DedicatedServerModInitializer
import org.bitecodelabs.botlerdaemon.connections.SocketClient
import org.bitecodelabs.botlerdaemon.events.PlayerLoginHandler
import org.bitecodelabs.botlerdaemon.events.PlayerLogoutHandler
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class Daemon : DedicatedServerModInitializer  {
    companion object {

        private const val MOD_ID = "Botler Daemon"

        @JvmField
        var LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

        var SOCKET: SocketClient = SocketClient()
    }
    override fun onInitializeServer() {

        LOGGER.info("Starting.....")

        ServerPlayConnectionEvents.JOIN.register(PlayerLoginHandler())

        ServerPlayConnectionEvents.DISCONNECT.register(PlayerLogoutHandler())

    }
}