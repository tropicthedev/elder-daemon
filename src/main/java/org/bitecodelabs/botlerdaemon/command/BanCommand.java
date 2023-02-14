package org.bitecodelabs.botlerdaemon.command;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.bitecodelabs.botlerdaemon.Daemon;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.connections.Command;
import org.bitecodelabs.botlerdaemon.connections.SocketClient;

public class BanCommand implements Command {

    @Override
    public void execute(GameProfile gameProfile, MinecraftServer server) {

        try {
            BannedPlayerList bannedPlayerList = server.getPlayerManager().getUserBanList();

            if (bannedPlayerList.contains(gameProfile)) {
                Daemon.LOGGER.info(gameProfile.getName() + " is already banned");
            } else {

                BannedPlayerEntry bannedPlayerEntry = new BannedPlayerEntry(
                        gameProfile,
                        null,
                        null,
                        null,
                        null
                );

                bannedPlayerList.add(bannedPlayerEntry);

                ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(gameProfile.getId());

                if (serverPlayerEntity != null) {

                    serverPlayerEntity.networkHandler.disconnect(Text.translatable("multiplayer.disconnect.banned"));

                }

                SocketClient.getInstance(server).emitSuccessEvent(String.valueOf(Config.SocketEvents.BOTLER_MEMBER_BAN_SUCCESS), gameProfile.getId());

                Daemon.LOGGER.info(gameProfile.getName() + " has been banned ");
            }

        } catch (Exception e) {
            Daemon.LOGGER.error(e.getMessage());
        }
    }
}


