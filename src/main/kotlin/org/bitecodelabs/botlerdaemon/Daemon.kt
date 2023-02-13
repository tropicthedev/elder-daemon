package org.bitecodelabs.botlerdaemon

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import org.bitecodelabs.botlerdaemon.events.PlayerLoginHandler
import org.bitecodelabs.botlerdaemon.events.PlayerLogoutHandler
import org.bitecodelabs.botlerdaemon.events.ServerStartHandler

class Daemon : DedicatedServerModInitializer  {
    companion object {

        private const val MOD_ID = "Botler Daemon"

        @JvmField
        var LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    }
    override fun onInitializeServer() {

        LOGGER.info("Starting.....")

        ServerPlayConnectionEvents.JOIN.register(PlayerLoginHandler())

        ServerPlayConnectionEvents.DISCONNECT.register(PlayerLogoutHandler())

        ServerLifecycleEvents.SERVER_STARTED.register(ServerStartHandler())
    }

}