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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static java.util.Collections.singletonMap;

public class SocketClient {

    Command command;
    private static SocketClient instance;
    private Socket socket;

    private SocketClient(MinecraftServer server) {

        try {

            String socketUrl = Config.HOST;

            IO.Options options = IO.Options.builder().setAuth(singletonMap("token", Config.API_TOKEN)).build();

            Socket socket = IO.socket(socketUrl, options);

            socket.connect();

            socket.on(Socket.EVENT_CONNECT, args -> Daemon.LOGGER.info("Connected"));

            socket.on(Socket.EVENT_DISCONNECT, args -> Daemon.LOGGER.info("Disconnected from botler socket"));

            socket.on(Socket.EVENT_CONNECT_ERROR, args -> Daemon.LOGGER.error(Arrays.toString(args)));

            socket.on(String.valueOf(Config.SocketEvents.SUCCESS), args -> {
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

            socket.on(Config.SocketEvents.GUARDIAN_MEMBER_ADD.getEvent(), args -> {

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

            socket.on(Config.SocketEvents.GUARDIAN_MEMBER_LEAVE.getEvent(), args -> {

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

            socket.on(Config.SocketEvents.GUARDIAN_MEMBER_BAN.getEvent(), args -> {

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

        } catch (Exception e) {

            Daemon.LOGGER.error(e.getMessage());

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

    public void emitSuccessEvent(String eventName, String message, Boolean bool) {

        JSONObject json = new JSONObject();

        try {
            json.put("success", bool);

            json.put("server_id", Config.SERVER_ID);

            json.put("msg", message);

            socket.emit(eventName, json);
        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }

    public void emitBanEvent(String eventName, String player, String reason)  {

        JSONObject json = new JSONObject();

        try {
            json.put("mojang_id", player);

            json.put("reason", reason);

            socket.emit(eventName, json);

        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }

    public void emitJoinEvent(String eventName, String player)  {

        JSONObject json = new JSONObject();

        try {
            json.put("mojang_id", player);

            json.put("server_id", Config.SERVER_ID);

            socket.emit(eventName, json);

        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }


    public void emitLeaveEvent(String eventName, String player)  {

        JSONObject json = new JSONObject();

        try {
            json.put("mojang_id", player);

            json.put("server_id", Config.SERVER_ID);

            socket.emit(eventName, json);

        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

    }

    public static SocketClient getInstance(MinecraftServer parameter) {

        if (instance == null) {

            instance = new SocketClient(parameter);

        }

        return instance;
    }
}