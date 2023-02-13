package org.bitecodelabs.botlerdaemon.connections;

import okhttp3.Call;
import okhttp3.Response;
import org.bitecodelabs.botlerdaemon.Daemon;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Callback implements okhttp3.Callback {
    @Override
    public void onFailure(@NotNull Call call, IOException e) {
        Daemon.LOGGER.error(e.getMessage());
    }

    @Override
    public void onResponse(@NotNull Call call, Response response) {
        response.close();
    }

}
