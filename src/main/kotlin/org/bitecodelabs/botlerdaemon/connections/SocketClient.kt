package org.bitecodelabs.botlerdaemon.connections

import java.util.*
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import org.bitecodelabs.botlerdaemon.Daemon
import org.bitecodelabs.botlerdaemon.config.Config

class SocketClient() {

    private lateinit var socket: Socket
    init {

        try {

            val socketUrl = Config.BOTLER_WEBSOCKET_HOST

            val options = IO.Options.builder()
                .setAuth(Collections.singletonMap("token", Config.BOTLER_API_TOKEN))
                .build()

            socket = IO.socket(socketUrl, options)

            socket.connect()

            socket.on(Socket.EVENT_CONNECT) { Daemon.LOGGER.info("Connected") }

            socket.on(Socket.EVENT_DISCONNECT) { Daemon.LOGGER.info("Disconnected from botler socket") }

            socket.on(Socket.EVENT_CONNECT_ERROR) { args: Array<Any?>? -> Daemon.LOGGER.error(Arrays.toString(args)) }

        } catch (e: URISyntaxException) {

            Daemon.LOGGER.error("Exception: " + e.message)

        } catch (e: Exception) {

            Daemon.LOGGER.error("Exception: " + e.message)

        }
    }

    fun emitEvent(eventName: String, data: String) {

        socket.emit(eventName, data)

    }
}