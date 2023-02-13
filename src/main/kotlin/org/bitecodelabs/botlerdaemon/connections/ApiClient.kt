package org.bitecodelabs.botlerdaemon.connections

import okhttp3.*
import org.bitecodelabs.botlerdaemon.Daemon
import org.bitecodelabs.botlerdaemon.config.Config
import java.io.IOException

class ApiClient {

    private val client = OkHttpClient()

    private val mediaType = MediaType.get("application/json; charset=utf-8")

    private val bearerToken: String = Config.BOTLER_API_TOKEN

    //TODO Investigate not requiring an empty string to prevent errors
    fun post(path: String, callback: Callback) {

        val body = RequestBody.create(mediaType, "")

        val request = Request.Builder()
            .url(Config.HOST + "/v1/guild" + path)
            .post(body)
            .addHeader("Authorization", bearerToken)
            .build()

        client.newCall(request).enqueue(callback)

    }

    fun patch(path: String, callback: Callback) {

        val body = RequestBody.create(mediaType, "")

        val request = Request.Builder()
            .url(Config.HOST + "/v1/guild" + path)
            .patch(body)
            .addHeader("Authorization", bearerToken)
            .build()

        client.newCall(request).enqueue(callback)

    }

}

class Callback : okhttp3.Callback {
    override fun onFailure(call: Call, e: IOException) {
        Daemon.LOGGER.error(e.message)
    }

    override fun onResponse(call: Call, response: Response) {
        response.close()
    }
}