package ru.tutu

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*

val SERVER_URL = "http://localhost:8081"

suspend fun getFirstState(userId:String, clientStorage: Map<String, ClientValue>): FirstResponse {
    return ktorClient.post<String>("$SERVER_URL/${SERVER_PATH_FIRST_REQUEST}"){
        body = TextContent(FirstRequestBody(userId, clientStorage).toJson(), ContentType.Application.Json)
    }.parseToFirstResponse()
}

suspend fun networkReducer(sessionId: String, clientStorage: Map<String, ClientValue>, intent: Intent): Node =
    ktorClient.post<String>("$SERVER_URL/$SERVER_PATH_NETWORK_REDUCER"){
        body = TextContent(NetworkReducerRequestBody(sessionId, clientStorage, intent).toJson(), ContentType.Application.Json)
    }.parseToReducerResult().state
