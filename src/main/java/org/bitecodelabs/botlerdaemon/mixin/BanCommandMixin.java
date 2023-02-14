package org.bitecodelabs.botlerdaemon.mixin;

import java.util.Collection;
import net.minecraft.text.Text;
import com.mojang.authlib.GameProfile;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.BanCommand;
import org.bitecodelabs.botlerdaemon.connections.SocketClient;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BanCommand.class)
public abstract class BanCommandMixin {

    @Inject(method = "ban", at = @At("HEAD"))
    private static void ban(ServerCommandSource source, Collection<GameProfile> targets, Text reason, CallbackInfoReturnable<Integer> cir) {

        SocketClient socketClient = SocketClient.getInstance(source.getServer());

        for (GameProfile gameProfile : targets) {

            socketClient.emitBanEvent(Config.SocketEvents.DAEMON_MEMBER_BAN.getEvent(), gameProfile.getId().toString(), String.valueOf(reason));

        }
    }
}