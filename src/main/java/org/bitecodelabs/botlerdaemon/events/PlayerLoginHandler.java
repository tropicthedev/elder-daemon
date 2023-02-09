package org.bitecodelabs.botlerdaemon.events;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.bitecodelabs.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.websocket.SocketClient;

import java.util.HashMap;
import java.util.Map;

public class PlayerLoginHandler implements ServerPlayConnectionEvents.Join {

    public static Map<String, String> createPlayerJoinEvent(String memberId, String serverId, String mcServerId, String sessionStart) {
        Map<String, String> playerJoinEvent = new HashMap<>();
        playerJoinEvent.put("member_id", memberId);
        playerJoinEvent.put("server_id", serverId);
        playerJoinEvent.put("mc_server_id", mcServerId);
        playerJoinEvent.put("session_start", sessionStart);

        return playerJoinEvent;
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

        final String player_uuid = handler.getPlayer().getUuidAsString();
        final String server_id = Config.BOTLER_DISCORD_SERVER_ID;
        final String mc_server_id = Config.BOTLER_MC_SERVER_ID;
        final String session_start = String.valueOf(System.currentTimeMillis());

        SocketClient socketClient = SocketClient.getInstance();

        Map<String, String> data = createPlayerJoinEvent(player_uuid, server_id, mc_server_id, session_start);

        socketClient.emitPlayerEvent(String.valueOf(SocketClient.Events.BOTLER_SERVER_MEMBER_JOIN), data);

        BotlerDaemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has joined the server");

    }
}


