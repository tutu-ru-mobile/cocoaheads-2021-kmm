package ru.tutu

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import ru.tutu.serialization.*

suspend fun networkReducer(
    userId: String,
    networkReducerUrl: String,
    clientStorage: ClientStorage,
    intent: Intent
): Result<NetworkReducerResult> = try {
    val successResult = ktorClient.post<String>(networkReducerUrl) {
        body =
            TextContent(NetworkReducerRequestBody(userId, clientStorage, intent).toJson(), ContentType.Application.Json)
    }.parseToNetworkReducerResult()
    Result.success(successResult)
} catch (t: Throwable) {
    Result.failure(t)
}
