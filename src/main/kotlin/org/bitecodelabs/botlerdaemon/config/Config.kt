package org.bitecodelabs.botlerdaemon.config

import java.util.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import net.fabricmc.loader.api.FabricLoader

object Config {

    var BOTLER_API_TOKEN: String? = null

    var BOTLER_WEBSOCKET_HOST: String? = null

    init {

        val properties = Properties()

        val newProperties = Properties()

        val path = FabricLoader.getInstance().configDir.resolve("botler.properties")

        if (Files.isRegularFile(path)) {

            try {

                properties.load(Files.newInputStream(path, StandardOpenOption.CREATE))

            } catch (e: IOException) {

                throw RuntimeException(e)

            }
        }
        BOTLER_WEBSOCKET_HOST = getString(properties, newProperties, "botler_websocket_host", "ws://localhost:3000")

        BOTLER_API_TOKEN = getString(properties, newProperties, "botler_api_token", "insert_api_token_here")

        try {

            newProperties.store(
                Files.newOutputStream(
                    path,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                ), "Configuration file for Botler Daemon"
            )

        } catch (e: IOException) {

            throw RuntimeException(e)

        }
    }

    private fun getString(properties: Properties, newProperties: Properties, key: String, def: String): String {

        return try {

            val property = properties.getProperty(key)

            newProperties.setProperty(key, property)

            property

        } catch (e: Exception) {

            newProperties.setProperty(key, def)

            def
        }
    }
}