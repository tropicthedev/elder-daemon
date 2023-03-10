package com.github.tropicdev.elderdaemon.connections;

import com.github.tropicdev.elderdaemon.Daemon;
import com.github.tropicdev.elderdaemon.config.Config;
import com.mojang.authlib.GameProfile;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.server.*;
import com.github.tropicdev.elderdaemon.command.WhitelistAddCommand;
import com.github.tropicdev.elderdaemon.command.WhitelistRemoveCommand;
import com.github.tropicdev.elderdaemon.command.BanCommand;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.*;

public class SocketClient {

    Command command;
    private static SocketClient instance;
    private Socket socket;

    private SocketClient(MinecraftServer server) {

        try {

            String socketUrl = Config.HOST;

            Map<String, String> authParams = new HashMap<>();
            authParams.put("token", Config.API_TOKEN);
            authParams.put("server_id", Config.SERVER_ID);

            IO.Options options = IO.Options.builder()
                    .setAuth(authParams)
                    .setReconnection(true)
                    .setReconnectionAttempts(20)
                    .setReconnectionDelay(1_000)
                    .setReconnectionDelayMax(5_000)
                    .setRandomizationFactor(0.5)
                    .setTimeout(20_000)
                    .build();

            socket = IO.socket(socketUrl, options);

            socket.connect();

            socket.on(Socket.EVENT_CONNECT, args -> Daemon.LOGGER.info("Connected to guardian"));

            socket.on(Socket.EVENT_DISCONNECT, args -> Daemon.LOGGER.info("Disconnected from guardian"));

            socket.on(Socket.EVENT_CONNECT_ERROR, args -> Daemon.LOGGER.error(Arrays.toString(args)));

            socket.on("success", args -> {
                for (Object arg : args) {
                    try {
                        JSONObject json = new JSONObject(arg.toString());

                        Boolean success = (Boolean) json.get("success");

                        String msg = (String) json.get("msg");

                        if (success) {
                            Daemon.LOGGER.info(msg);
                        } else {
                            Daemon.LOGGER.warn(msg);
                        }

                    } catch (Exception e) {
                        Daemon.LOGGER.error(e.getMessage());
                    }
                }
            });

            socket.on("add", args -> {

                for (Object arg : args) {

                    try {
                        JSONObject json = new JSONObject(arg.toString());

                        String id = (String) json.get("id");

                        String name = (String) json.get("name");

                        UUID uuid = UUID.fromString(id);

                        GameProfile gameProfile = new GameProfile(uuid, name);

                        SocketClient.instance.setCommand(new WhitelistAddCommand());

                        SocketClient.instance.executeCommand(gameProfile, server);

                    } catch (Exception e) {
                        Daemon.LOGGER.error(e.getMessage());
                    }
                }
            });

            socket.on("leave", args -> {

                for (Object arg : args) {
                    try{
                        JSONObject json = new JSONObject(arg.toString());

                        String id = (String) json.get("id");

                        String name = (String) json.get("name");

                        UUID uuid = UUID.fromString(id);

                        GameProfile gameProfile = new GameProfile(uuid, name);

                        SocketClient.instance.setCommand(new WhitelistRemoveCommand());

                        SocketClient.instance.executeCommand(gameProfile, server);

                    } catch (Exception e) {
                        Daemon.LOGGER.error(e.getMessage());
                    }
                }
            });

            socket.on("ban", args -> {

                for (Object arg : args) {

                    try{
                        JSONObject json = new JSONObject(arg.toString());

                        String id = (String) json.get("id");

                        String name = (String) json.get("name");

                        UUID uuid = UUID.fromString(id);

                        GameProfile gameProfile = new GameProfile(uuid, name);

                        SocketClient.instance.setCommand(new BanCommand());

                        SocketClient.instance.executeCommand(gameProfile, server);

                    } catch (Exception e) {
                        Daemon.LOGGER.error(e.getMessage());
                    }
                }
            });

        } catch (URISyntaxException e) {

            Daemon.LOGGER.error(e.getMessage());
            Daemon.LOGGER.warn("Guardian is disconnected");
        }
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand(GameProfile gameProfile, MinecraftServer server) {
        try {
            command.execute(gameProfile, server);
        } catch (Exception e) {
            Daemon.LOGGER.error(e.getMessage());
        }
    }

    public void emitSuccessEvent(String message, Boolean bool) {

        JSONObject json = new JSONObject();

        try {
            json.put("success", bool);

            json.put("server_id", Config.SERVER_ID);

            json.put("msg", message);

            socket.emit("success", json);
        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }

    public void emitBanEvent(GameProfile player, @Nullable Text reason)  {

        JSONObject json = new JSONObject();

        try {
            json.put("id", player.getId().toString());

            json.put("name", player.getName());

            json.put("reason", reason);

        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

        socket.emit("ban", json);

    }

    public void emitJoinEvent( String player)  {

        JSONObject json = new JSONObject();

        try {
            json.put("mojang_id", player);

            json.put("server_id", Config.SERVER_ID);

            socket.emit("session-start", json);

        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }


    public void emitLeaveEvent( String player)  {

        JSONObject json = new JSONObject();

        try {
            json.put("mojang_id", player);

            json.put("server_id", Config.SERVER_ID);

            socket.emit("session-end", json);

        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }

    public void closeInstance() {
       socket.close();
    };

    public static SocketClient getInstance(MinecraftServer parameter) {

        if (instance == null) {

            instance = new SocketClient(parameter);

        }

        return instance;
    }


}