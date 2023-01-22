package org.bitecodelabs.com.botlerdaemon.api;

import org.bitecodelabs.com.botlerdaemon.BotlerDaemon;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SocratesRequester {
    public static final String SOCRATES_ENDPOINT = "http://193.122.137.135:3000/ping";

    public static void main() {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SOCRATES_ENDPOINT)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(BotlerDaemon.LOGGER::info)
                .join();

    }

}
