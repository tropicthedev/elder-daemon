package org.bitecodelabs.com.botlerdaemon.websocket;
import io.socket.client.IO;
        import io.socket.client.Socket;
        import io.socket.emitter.Emitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;

import org.bitecodelabs.com.botlerdaemon.config.Config;
import org.json.JSONException;
import org.json.JSONObject;

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
    }

}

