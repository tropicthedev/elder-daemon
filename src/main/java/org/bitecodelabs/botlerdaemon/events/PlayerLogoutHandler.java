package org.bitecodelabs.botlerdaemon.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.bitecodelabs.botlerdaemon.BotlerDaemon;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.websocket.SocketClient;

import java.util.HashMap;
import java.util.Map;

public class PlayerLogoutHandler implements ServerPlayConnectionEvents.Disconnect {
    public static Map<String, String> createPlayerLeaveEvent(String memberId, String serverId, String mcServerId, String sessionEnd) {
        Map<String, String> playerLeaveEvent = new HashMap<>();
        playerLeaveEvent.put("member_id", memberId);
        playerLeaveEvent.put("server_id", serverId);
        playerLeaveEvent.put("mc_server_id", mcServerId);
        playerLeaveEvent.put("session_end", sessionEnd);

        return playerLeaveEvent;
    }
    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        final String player_uuid = handler.getPlayer().getUuidAsString();
        final String server_id = Config.BOTLER_DISCORD_SERVER_ID;
        final String mc_server_id = Config.BOTLER_MC_SERVER_ID;
        final String session_end = String.valueOf(System.currentTimeMillis());

        SocketClient socketClient = SocketClient.getInstance();

        Map<String, String> data = createPlayerLeaveEvent(player_uuid, server_id, mc_server_id, session_end);

        socketClient.emitPlayerEvent(String.valueOf(SocketClient.Events.BOTLER_SERVER_MEMBER_JOIN), data);
        BotlerDaemon.LOGGER.info(handler.getPlayer().getUuidAsString() + " Has left the server");
    }
}
