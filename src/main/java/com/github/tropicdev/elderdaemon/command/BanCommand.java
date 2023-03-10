package com.github.tropicdev.elderdaemon.command;

import com.github.tropicdev.elderdaemon.Daemon;
import com.github.tropicdev.elderdaemon.config.Config;
import com.github.tropicdev.elderdaemon.connections.Command;
import com.github.tropicdev.elderdaemon.connections.SocketClient;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class BanCommand implements Command {

    @Override
    public void execute(GameProfile gameProfile, MinecraftServer server) {

        try {
            BannedPlayerList bannedPlayerList = server.getPlayerManager().getUserBanList();

            if (bannedPlayerList.contains(gameProfile)) {
                String msg = gameProfile.getName() + " is already banned";

                SocketClient.getInstance(server).emitSuccessEvent(msg, false);
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

                String msg = gameProfile.getName() + " Has been Banned From The Server";

                SocketClient.getInstance(server).emitSuccessEvent(msg, true);

                Daemon.LOGGER.info(gameProfile.getName() + " has been banned ");
            }

        } catch (Exception e) {
            Daemon.LOGGER.error(e.getMessage());

            String msg = gameProfile.getName() + " Could Not Be Banned From The Server";

            SocketClient.getInstance(server).emitSuccessEvent(msg, false);
        }
    }
}


