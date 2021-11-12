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

    /*
        ProtoBuf:
        val successResult = ktorClient.post<ByteArray>(networkReducerUrl) {
        body =
            ByteArrayContent(NetworkReducerRequestBody(userId, clientStorage, intent).toProtoBuff(), ContentType.Application.ProtoBuf)
     */
    }.parseToNetworkReducerResult()
    Result.success(successResult)
} catch (t: Throwable) {
    Result.failure(t)
}
