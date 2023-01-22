package org.bitecodelabs.com.botlerdaemon;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotlerDaemon implements ModInitializer {

    public static final String MOD_ID = "Botler Daemon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        System.out.println("Hello");
    }
}
