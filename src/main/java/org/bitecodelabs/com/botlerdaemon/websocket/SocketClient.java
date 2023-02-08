package org.bitecodelabs.com.botlerdaemon.websocket;
import io.socket.client.IO;
        import io.socket.client.Socket;
        import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class SocketClient {
    private static SocketClient instance;
    private Socket socket;

    public enum Events {
        BOTLER_SERVER_MEMBER_JOIN,
        BOTLER_SERVER_MEMBER_LEAVE,
        BOTLER_SERVER_MEMBER_BAN,
        BOTLER_SERVER_MEMBER_KICK
    }

    private SocketClient() {

        String socketUrl = System.getenv("SOCKET_URL");
        if (socketUrl == null) {
            socketUrl = "http://localhost:3000";
        }
        try {
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
    }

}

