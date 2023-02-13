package org.bitecodelabs.botlerdaemon.connections;

import com.mojang.authlib.GameProfile;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.bitecodelabs.botlerdaemon.Daemon;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class SocketClient {
    private static SocketClient instance;
    private Socket socket;

    private SocketClient(MinecraftServer server) {

        try {

            String socketUrl = Config.HOST;

            Map<String, String> auth = new HashMap<>();

            auth.put("token", Config.BOTLER_API_TOKEN);

            IO.Options options = IO.Options.builder().setAuth(auth).build();

            socket = IO.socket(socketUrl, options);

            socket.connect();

            socket.on(Socket.EVENT_CONNECT, args -> Daemon.LOGGER.info("Connected"));

            socket.on(Socket.EVENT_DISCONNECT, args -> Daemon.LOGGER.info("Disconnected from botler socket"));

            socket.on(Socket.EVENT_CONNECT_ERROR, args -> Daemon.LOGGER.error(Arrays.toString(args)));

            socket.on(String.valueOf(Config.SocketEvents.DAEMON_MEMBER_BAN_SUCCESS), args -> {

                MojangUser data = convertToData(args);

                if (data != null) {
                    Daemon.LOGGER.info(data.name() + " banned from discord through minecraft server");
                }
            });

            socket.on(Config.SocketEvents.BOTLER_MEMBER_ADD.getEvent(), args -> {

                MojangUser data = convertToData(args);

                if (data != null) {

                    GameProfile gameProfile = new GameProfile(data.uuid(), data.name());

                    Whitelist whitelist = server.getPlayerManager().getWhitelist();

                    if (!whitelist.isAllowed(gameProfile)) {

                        WhitelistEntry whitelistEntry = new WhitelistEntry(gameProfile);

                        whitelist.add(whitelistEntry);

                        try {

                            emitSuccessEvent(String.valueOf(Config.SocketEvents.BOTLER_MEMBER_ADD_SUCCESS), gameProfile.getId());

                        } catch (JSONException e) {

                            Daemon.LOGGER.error(e.getMessage());

                            return;
                        }

                        Daemon.LOGGER.info("Member " + data.name() + " has been added to the whitelist");
                    }
                }
            });

            socket.on(Config.SocketEvents.BOTLER_MEMBER_REMOVE.getEvent(), args -> {

                MojangUser data = convertToData(args);

                if (data != null) {

                    GameProfile gameProfile = new GameProfile(data.uuid(), data.name());

                    Whitelist whitelist = server.getPlayerManager().getWhitelist();

                    if (!whitelist.isAllowed(gameProfile)) {

                        WhitelistEntry whitelistEntry = new WhitelistEntry(gameProfile);

                        whitelist.remove(whitelistEntry);

                        try {

                            emitSuccessEvent(String.valueOf(Config.SocketEvents.BOTLER_MEMBER_REMOVE_SUCCESS), gameProfile.getId());

                        } catch (JSONException e) {

                            Daemon.LOGGER.error(e.getMessage());

                            return;

                        }

                        Daemon.LOGGER.info("Member " + data.name() + " has been removed from the whitelist");
                    }
                }
            });

            socket.on(Config.SocketEvents.BOTLER_MEMBER_BAN.getEvent(), args -> {

                MojangUser data = convertToData(new List[]{Arrays.asList(args)});

            if (data != null) {

                BannedPlayerList bannedPlayerList = server.getPlayerManager().getUserBanList();

                GameProfile gameProfile = new GameProfile(data.uuid(), data.name());

                if (bannedPlayerList.contains(gameProfile)) {
                    Daemon.LOGGER.info(gameProfile.getName() + " is already banned");
                } else {

                    BannedPlayerEntry bannedPlayerEntry = new BannedPlayerEntry(
                            gameProfile,
                            null,
                            null,
                            null,
                            data.reason()
                    );

                    bannedPlayerList.add(bannedPlayerEntry);

                    ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(gameProfile.getId());

                    if (serverPlayerEntity != null) {

                        serverPlayerEntity.networkHandler.disconnect(Text.translatable("multiplayer.disconnect.banned"));

                    }

                    try {

                        emitSuccessEvent(String.valueOf(Config.SocketEvents.BOTLER_MEMBER_BAN_SUCCESS), gameProfile.getId());

                    } catch (JSONException e) {

                        Daemon.LOGGER.error(e.getMessage());
                    }

                    Daemon.LOGGER.info(gameProfile.getName() + " has been banned for reason: " + data.reason());
                }
            }
        });

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

    private record MojangUser(String name, UUID uuid, @Nullable String reason) {

        @Override
        public String reason() {

            return reason;
            }
        }

    private MojangUser convertToData(Object[] array) {

        Object element = array[1];

        try {

            return new MojangUser((String) array[0], UUID.fromString((String) element), (String) array[2]);

        } catch (Exception e) {

            return null;
        }
    }

    private void emitSuccessEvent(String eventName, UUID player) throws JSONException {

        JSONObject json = new JSONObject();

        json.put("guildId", Config.BOTLER_GUILD_ID);

        json.put("serverId", Config.BOTLER_SERVER_ID);

        json.put("playerId", player);

        socket.emit(eventName, json);
    }

    public void emitBanEvent(String eventName, String player, String reason) throws JSONException {

        JSONObject json = new JSONObject();

        json.put("guildId", Config.BOTLER_GUILD_ID);

        json.put("serverId", Config.BOTLER_SERVER_ID);

        json.put("playerId", player);

        json.put("reason", reason);

        socket.emit(eventName, json);
    }
}