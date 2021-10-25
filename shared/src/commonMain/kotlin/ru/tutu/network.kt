package ru.tutu

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import ru.tutu.serialization.*

val SERVER_URL = "http://localhost:8081"

suspend fun networkReducer(userId: String, clientStorage: ClientStorage, intent: Intent): ViewTreeNode =
    ktorClient.post<String>("$SERVER_URL/$SERVER_PATH_NETWORK_REDUCER"){
        body = TextContent(NetworkReducerRequestBody(userId, clientStorage, intent).toJson(), ContentType.Application.Json)
    }.parseToNetworkReducerResult().state
