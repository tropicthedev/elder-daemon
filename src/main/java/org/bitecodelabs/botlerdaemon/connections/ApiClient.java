package org.bitecodelabs.botlerdaemon.connections;

import okhttp3.*;
import org.bitecodelabs.botlerdaemon.config.Config;


public class ApiClient {

    private final OkHttpClient client = new OkHttpClient();

    private final MediaType mediaType = MediaType.get("application/json; charset=utf-8");

    private final String bearerToken = Config.BOTLER_API_TOKEN;

    public void post(String path, Callback callback) {
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
            .url(Config.HOST + "/v1/guild" + path)
            .post(body)
            .addHeader("Authorization", bearerToken)
            .build();

        client.newCall(request).enqueue(callback);
    }

    public void patch(String path, Callback callback) {
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
            .url(Config.HOST + "/v1/guild" + path)
            .patch(body)
            .addHeader("Authorization", bearerToken)
            .build();

        client.newCall(request).enqueue(callback);
    }

}

