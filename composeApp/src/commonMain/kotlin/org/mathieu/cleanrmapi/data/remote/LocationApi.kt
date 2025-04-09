package org.mathieu.cleanrmapi.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.mathieu.cleanrmapi.data.remote.responses.LocationResponse

internal class LocationApi(private val client: HttpClient) {

    suspend fun getLocation(id: Int): LocationResponse? {
        return client
            .get("location/$id")
            .accept(HttpStatusCode.OK)
            .body()
    }
}