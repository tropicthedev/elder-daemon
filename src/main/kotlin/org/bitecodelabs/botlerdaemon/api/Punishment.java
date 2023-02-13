package org.bitecodelabs.botlerdaemon.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;

public class Punishment {

    public interface BanPlayerCallback {

        Event<BanPlayerCallback> EVENT = EventFactory.createArrayBacked(BanPlayerCallback.class,
                (listeners) -> (player, server) -> {
                    for (BanPlayerCallback listener : listeners) {
                        ActionResult result = listener.interact(player, server);

                        if(result != ActionResult.PASS) {
                            return result;
                        }
                    }

                    return ActionResult.PASS;
                });

        ActionResult interact(BannedPlayerEntry player, MinecraftServer server);
    }
}
