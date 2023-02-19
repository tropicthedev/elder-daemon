package org.bitecodelabs.botlerdaemon.connections;

import com.mojang.authlib.GameProfile;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.server.*;
import org.bitecodelabs.botlerdaemon.Daemon;
import org.bitecodelabs.botlerdaemon.command.WhitelistAddCommand;
import org.bitecodelabs.botlerdaemon.command.WhitelistRemoveCommand;
import org.bitecodelabs.botlerdaemon.command.BanCommand;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class SocketClient {

    Command command;
    private static SocketClient instance;
    private Socket socket;

    private SocketClient(MinecraftServer server) {

        try {

            String socketUrl = Config.HOST;

            IO.Options options = new IO.Options();

            options.query = "guildId=" + Config.BOTLER_GUILD_ID + "&serverId=" + Config.BOTLER_SERVER_ID + "&token=" + Config.BOTLER_API_TOKEN;

            socket = IO.socket(socketUrl, options);

            socket.connect();

            socket.on(Socket.EVENT_CONNECT, args -> Daemon.LOGGER.info("Connected"));

            socket.on(Socket.EVENT_DISCONNECT, args -> Daemon.LOGGER.info("Disconnected from botler socket"));

            socket.on(Socket.EVENT_CONNECT_ERROR, args -> Daemon.LOGGER.error(Arrays.toString(args)));

            socket.on(String.valueOf(Config.SocketEvents.DAEMON_MEMBER_BAN_SUCCESS), args -> {

            });

            socket.on(Config.SocketEvents.BOTLER_MEMBER_ADD.getEvent(), args -> {

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

            socket.on(Config.SocketEvents.BOTLER_MEMBER_REMOVE.getEvent(), args -> {

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

            socket.on(Config.SocketEvents.BOTLER_MEMBER_BAN.getEvent(), args -> {

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

    public static SocketClient getInstance(MinecraftServer parameter) {

        if (instance == null) {

            instance = new SocketClient(parameter);

        }

        return instance;
    }

    public void emitSuccessEvent(String eventName, UUID player) {

        JSONObject json = new JSONObject();

        try {
            json.put("guildId", Config.BOTLER_GUILD_ID);

            json.put("serverId", Config.BOTLER_SERVER_ID);

            json.put("playerId", player);
        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

        socket.emit(eventName, json);
    }

    public void emitBanEvent(String eventName, String player, String reason)  {

        JSONObject json = new JSONObject();

        try {
            json.put("guildId", Config.BOTLER_GUILD_ID);

            json.put("serverId", Config.BOTLER_SERVER_ID);

            json.put("playerId", player);

            json.put("reason", reason);

        } catch (JSONException e) {
            Daemon.LOGGER.error(e.getMessage());
        }

        socket.emit(eventName, json);
    }
}