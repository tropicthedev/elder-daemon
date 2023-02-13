package org.bitecodelabs.botlerdaemon.config

import java.util.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import net.fabricmc.loader.api.FabricLoader

object Config {

    var BOTLER_API_TOKEN: String

    var BOTLER_SERVER_ID: String

    var BOTLER_GUILD_ID: String

    var HOST: String

    enum class SocketEvents(val event: String) {
        BOTLER_MEMBER_ADD("BOTLER_SERVER_MEMBER_ADD"),
        BOTLER_MEMBER_ADD_SUCCESS("BOTLER_MEMBER_ADD_SUCCESS"),
        BOTLER_MEMBER_REMOVE("BOTLER_SERVER_MEMBER_REMOVE"),
        BOTLER_MEMBER_REMOVE_SUCCESS("BOTLER_MEMBER_REMOVE_SUCCESS"),
        BOTLER_MEMBER_BAN("BOTLER_SERVER_MEMBER_BAN"),
        BOTLER_MEMBER_BAN_SUCCESS("BOTLER_SERVER_MEMBER_BAN_SUCCESS"),
        DAEMON_MEMBER_BAN("DAEMON_SERVER_MEMBER_BAN"),
        DAEMON_MEMBER_BAN_SUCCESS("DAEMON_SERVER_MEMBER_BAN"),
    }

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
        HOST = getString(properties, newProperties, "botler_host", "ws://localhost:3000")

        BOTLER_API_TOKEN = getString(properties, newProperties, "botler_api_token", "insert_api_token_here")

        BOTLER_SERVER_ID = getString(properties, newProperties, "botler_server_id", "insert_server_id_here")

        BOTLER_GUILD_ID = getString(properties, newProperties, "botler_guild_id", "insert_guild_id_here")

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