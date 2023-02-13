package org.bitecodelabs.botlerdaemon.connections

import com.mojang.authlib.GameProfile
import io.socket.client.IO
import io.socket.client.Socket
import net.minecraft.server.*
import net.minecraft.text.Text
import net.minecraft.text.Texts
import org.bitecodelabs.botlerdaemon.Daemon
import org.bitecodelabs.botlerdaemon.config.Config
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

class SocketClient private constructor(private val server: MinecraftServer) {

    companion object {
        private var instance: SocketClient? = null

        fun getInstance(parameter: MinecraftServer): SocketClient {
            if (instance == null) {
                instance = SocketClient(parameter)
            }
            return instance as SocketClient
        }
    }

    data class MojangUser(val name: String , val id: UUID, val reason: String?)

    private lateinit var socket: Socket
    init {

        try {

            val socketUrl = Config.HOST

            val options = IO.Options.builder()
                .setAuth(Collections.singletonMap("token", Config.BOTLER_API_TOKEN))
                .build()

            socket = IO.socket(socketUrl, options)

            socket.connect()

            socket.on(Socket.EVENT_CONNECT) { Daemon.LOGGER.info("Connected") }

            socket.on(Socket.EVENT_DISCONNECT) { Daemon.LOGGER.info("Disconnected from botler socket") }

            socket.on(Socket.EVENT_CONNECT_ERROR) { args: Array<Any?>? -> Daemon.LOGGER.error(Arrays.toString(args)) }

            socket.on(Config.SocketEvents.DAEMON_MEMBER_BAN_SUCCESS.event) { args: Array<Any?>? ->

                val data = args?.let { convertToData(it) }

                if(data != null) {
                    Daemon.LOGGER.info("${data.name} banned from discord through minecraft server")
                }
            }

            socket.on(Config.SocketEvents.BOTLER_MEMBER_ADD.event) { args: Array<Any?>? ->

                val data = args?.let { convertToData(it) }

                if (data != null) {

                    val gameProfile = GameProfile(data.id, data.name)

                    val whitelist: Whitelist = server.playerManager.whitelist

                        if (!whitelist.isAllowed(gameProfile)) {

                            val whitelistEntry = WhitelistEntry(gameProfile)

                            whitelist.add(whitelistEntry)

                            socket.emit(Config.SocketEvents.BOTLER_MEMBER_ADD_SUCCESS.event, "success")

                            Daemon.LOGGER.info("Member ${data.name} has been added to the whitelist")
                        }
                }
            }

            socket.on(Config.SocketEvents.BOTLER_MEMBER_REMOVE.event) { args: Array<Any?>? ->

                val data = args?.let { convertToData(it) }

                if(data != null) {

                    val gameProfile = GameProfile(data.id, data.name)

                    val whitelist: Whitelist = server.playerManager.whitelist

                    if (!whitelist.isAllowed(gameProfile)) {

                        val whitelistEntry = WhitelistEntry(gameProfile)

                        whitelist.remove(whitelistEntry)

                        socket.emit(Config.SocketEvents.BOTLER_MEMBER_REMOVE_SUCCESS.event, "success")

                        Daemon.LOGGER.info("Member ${data.name} has been added to the whitelist")
                    }

                }

            }

            socket.on(Config.SocketEvents.BOTLER_MEMBER_BAN.event) { args: Array<Any?>? ->

                val data = args?.let { convertToData(it) }

                if (data != null) {

                    val bannedPlayerList = server.playerManager.userBanList

                    val gameProfile = GameProfile(data.id, data.name)

                    if(!bannedPlayerList.contains(gameProfile)) {
                        val bannedPlayerEntry = BannedPlayerEntry(
                            gameProfile,
                            null as Date?,
                            null as String?,
                            null as Date?,
                            data.reason
                        )
                        bannedPlayerList.add(bannedPlayerEntry)

                        val serverPlayerEntity = server.playerManager.getPlayer(gameProfile.id)

                        serverPlayerEntity?.networkHandler?.disconnect(Text.translatable("multiplayer.disconnect.banned"))

                        socket.emit(Config.SocketEvents.BOTLER_MEMBER_BAN_SUCCESS.event, "success")

                        Daemon.LOGGER.info("${gameProfile.name} has been banned for reason: ${data.reason}")
                    }

                    Daemon.LOGGER.info("${gameProfile.name} is already banned")

                }
            }

        } catch (e: URISyntaxException) {

            Daemon.LOGGER.error("Exception: " + e.message)

        } catch (e: Exception) {

            Daemon.LOGGER.error("Exception: " + e.message)

        }
    }
    private fun convertToData(array: Array<Any?>): MojangUser? {
        val element = array[1]
        return try {
            MojangUser(array[0] as String, UUID.fromString(element as String), array[2] as String)
        } catch (e: Exception) {
            null
        }
    }


    fun emitEvent(eventName: String, data: String) {

        socket.emit(eventName, data)

    }
}