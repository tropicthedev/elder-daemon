package org.bitecodelabs.botlerdaemon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.bitecodelabs.botlerdaemon.config.Config;
import org.bitecodelabs.botlerdaemon.event.PlayerLoginHandler;
import org.bitecodelabs.botlerdaemon.event.PlayerLogoutHandler;
import org.bitecodelabs.botlerdaemon.websocket.SocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotlerDaemon implements ModInitializer {

    public static final String MOD_ID = "Botler Daemon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {

        BotlerDaemon.LOGGER.info("Starting Botler Daemon");
        BotlerDaemon.LOGGER.info(Config.BOTLER_WEBSOCKET_HOST);

        SocketClient socketClient = SocketClient.getInstance();

        socketClient.emitEvent(String.valueOf(SocketClient.Events.BOTLER_SERVER_START), Config.BOTLER_SERVER_NAME);

        ServerPlayConnectionEvents.JOIN.register((new PlayerLoginHandler()));

        ServerPlayConnectionEvents.DISCONNECT.register((new PlayerLogoutHandler()));

    }
}
