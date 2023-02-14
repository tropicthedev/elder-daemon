package org.bitecodelabs.botlerdaemon.connections;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;

public interface Command {

    void execute(GameProfile gameProfile, MinecraftServer server);
}
