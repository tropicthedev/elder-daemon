package com.github.tropicdev.elderdaemon.command;

import com.github.tropicdev.elderdaemon.Daemon;
import com.github.tropicdev.elderdaemon.connections.Command;
import com.github.tropicdev.elderdaemon.connections.SocketClient;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.Whitelist;
import net.minecraft.server.WhitelistEntry;
import net.minecraft.server.MinecraftServer;

public class WhitelistAddCommand implements Command {

    @Override
    public void execute(GameProfile gameProfile, MinecraftServer server) {

        try {
            Whitelist whitelist = server.getPlayerManager().getWhitelist();

            if (!whitelist.isAllowed(gameProfile)) {

                WhitelistEntry whitelistEntry = new WhitelistEntry(gameProfile);

                whitelist.add(whitelistEntry);

                String msg = gameProfile.getName() + " was added to the whitelist";

                SocketClient.getInstance(server).emitSuccessEvent(msg, true);

                Daemon.LOGGER.info("Member " + gameProfile.getName() + " has been added to the whitelist");

            } else {
                String msg = gameProfile.getName() + " could not be added to the whitelist";

                SocketClient.getInstance(server).emitSuccessEvent(msg, false);
                Daemon.LOGGER.error("Member " + gameProfile.getName() + " could not be added to the whitelist");
            }

        } catch (Exception e) {
            String msg = gameProfile.getName() + " could not be added to the whitelist";

            SocketClient.getInstance(server).emitSuccessEvent(msg, false);
            Daemon.LOGGER.error(e.getMessage());
        }
    }
}
