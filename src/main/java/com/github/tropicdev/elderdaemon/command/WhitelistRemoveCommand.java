package com.github.tropicdev.elderdaemon.command;

import com.github.tropicdev.elderdaemon.Daemon;
import com.github.tropicdev.elderdaemon.config.Config;
import com.github.tropicdev.elderdaemon.connections.Command;
import com.github.tropicdev.elderdaemon.connections.SocketClient;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.Whitelist;
import net.minecraft.server.WhitelistEntry;
import net.minecraft.server.MinecraftServer;

public class WhitelistRemoveCommand implements Command {

    @Override
    public void execute(GameProfile gameProfile, MinecraftServer server) {

        try {
            Whitelist whitelist = server.getPlayerManager().getWhitelist();

            if (!whitelist.isAllowed(gameProfile)) {

                WhitelistEntry whitelistEntry = new WhitelistEntry(gameProfile);

                whitelist.remove(whitelistEntry);

                String msg = gameProfile.getName() + " has been removed from the whitelist";

                SocketClient.getInstance(server).emitSuccessEvent(String.valueOf(Config.SocketEvents.SUCCESS), msg, true);

                Daemon.LOGGER.info("Member " + gameProfile.getName() + " has been removed from the whitelist");

            } else {
                String msg = gameProfile.getName() + " could not be removed from the whitelist";

                SocketClient.getInstance(server).emitSuccessEvent(String.valueOf(Config.SocketEvents.SUCCESS), msg, false);

                Daemon.LOGGER.error("Member " + gameProfile.getName() + " could not be removed from the whitelist");
            }

        } catch (Exception e) {
            String msg = gameProfile.getName() + " could not be removed from the whitelist";

            SocketClient.getInstance(server).emitSuccessEvent(String.valueOf(Config.SocketEvents.SUCCESS), msg, false);
            Daemon.LOGGER.error(e.getMessage());
        }
    }
}
