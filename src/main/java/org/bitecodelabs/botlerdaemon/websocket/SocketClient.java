package org.bitecodelabs.botlerdaemon.websocket;
import io.socket.client.IO;
        import io.socket.client.Socket;
        import io.socket.emitter.Emitter;


import java.net.URISyntaxException;

import org.bitecodelabs.botlerdaemon.config.Config;


public class SocketClient {
    private static SocketClient instance;
    private Socket socket;
    public enum Events {
        BOTLER_SERVER_MEMBER_JOIN,
        BOTLER_SERVER_MEMBER_LEAVE,
        BOTLER_SERVER_MEMBER_BAN,
        BOTLER_SERVER_MEMBER_KICK,
        BOTLER_SERVER_START

    }

    private SocketClient() {

        try {
            String socketUrl = Config.BOTLER_WEBSOCKET_HOST;
            socket = IO.socket(socketUrl);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connected");
                    socket.emit("message", "Hello from Java client");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Disconnected");
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

}

