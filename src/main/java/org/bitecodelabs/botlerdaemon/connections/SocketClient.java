package org.bitecodelabs.botlerdaemon.connections;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.bitecodelabs.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.botlerdaemon.config.Config;
import java.net.URISyntaxException;
import java.util.Arrays;

import static java.util.Collections.singletonMap;

public class SocketClient {
    private Socket socket;

    public enum Events {
        BOTLER_SERVER_BAN
    }

    public SocketClient() {
        try {
            String socketUrl = Config.BOTLER_WEBSOCKET_HOST;

            IO.Options options = IO.Options.builder()
                    .setAuth(singletonMap("token", Config.BOTLER_API_TOKEN))
                    .build();

            socket = IO.socket(socketUrl, options);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        try {
            socket.connect();

            socket.on(Socket.EVENT_CONNECT, args -> BotlerDaemon.LOGGER.info("Connected"));

            socket.on(Socket.EVENT_DISCONNECT, args -> BotlerDaemon.LOGGER.info("Disconnected from botler socket"));

            socket.on(Socket.EVENT_CONNECT_ERROR, args -> BotlerDaemon.LOGGER.error(Arrays.toString(args)));

        } catch (Exception e) {
            BotlerDaemon.LOGGER.error("Exception: " + e.getMessage());
        }
    }

    public void emitEvent(String eventName, String data) {
        socket.emit(eventName, data);
    }

    public void emitBanEvent(Events botlerServerBan, String data) {
        socket.emit(botlerServerBan.toString(), data);
    }
}
