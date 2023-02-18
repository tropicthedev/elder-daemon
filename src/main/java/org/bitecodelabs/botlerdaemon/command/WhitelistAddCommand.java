package org.bitecodelabs.botlerdaemon.command;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.Whitelist;
import net.minecraft.server.WhitelistEntry;
import org.bitecodelabs.botlerdaemon.Daemon;
import net.minecraft.server.MinecraftServer;
import org.bitecodelabs.botlerdaemon.connections.Command;

public class WhitelistAddCommand implements Command {

    @Override
    public void execute(GameProfile gameProfile, MinecraftServer server) {

        try {
            Whitelist whitelist = server.getPlayerManager().getWhitelist();

            if (!whitelist.isAllowed(gameProfile)) {

                WhitelistEntry whitelistEntry = new WhitelistEntry(gameProfile);

                whitelist.add(whitelistEntry);

                Daemon.LOGGER.info("Member " + gameProfile.getName() + " has been added to the whitelist");

            } else {
                Daemon.LOGGER.error("Member " + gameProfile.getName() + " could not be added to the whitelist");
            }

        } catch (Exception e) {
            Daemon.LOGGER.error(e.getMessage());
        }
    }
}
