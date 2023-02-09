package org.bitecodelabs.botlerdaemon.websocket;
import io.socket.client.IO;
        import io.socket.client.Socket;
        import io.socket.emitter.Emitter;


import java.net.URISyntaxException;
import java.util.Map;

import org.bitecodelabs.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.botlerdaemon.config.Config;

import static java.util.Collections.singletonMap;


public class SocketClient {
    private static SocketClient instance;
    private Socket socket;
    public enum Events {
        BOTLER_SERVER_MEMBER_JOIN,
        BOTLER_SERVER_MEMBER_LEAVE,
        BOTLER_SERVER_MEMBER_BAN,
        BOTLER_SERVER_MEMBER_KICK,
        BOTLER_SERVER_START,
        BOTLER_SERVER_BOOT,
        BOTLER_SERVER_SHUTDOWN
    }

    private SocketClient() {

        try {
            String socketUrl = Config.BOTLER_WEBSOCKET_HOST;

            IO.Options options = IO.Options.builder()
                    .setAuth(Config.BOTLER_CREDENTIALS)
                    .build();

            socket = IO.socket(socketUrl,options);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    BotlerDaemon.LOGGER.info("Connected");
                    socket.emit("message", "Hello from Java client");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    BotlerDaemon.LOGGER.info("Disconnected from botler....ensure that your api token is valid");
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static SocketClient getInstance() {
        if (instance == null) {
            instance = new SocketClient();
        }
        return instance;
    }

    public void emitEvent(String eventName, String data) {
        socket.emit(eventName, data);
    };
    public void emitPlayerEvent(String eventName, Map<String, Object> data) {
        socket.emit(eventName, data);
    };


}

