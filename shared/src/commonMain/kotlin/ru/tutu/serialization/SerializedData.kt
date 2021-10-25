package ru.tutu.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun ViewTreeNode.toJson():String =
    Json.encodeToString(this)

fun String.parseToNode():ViewTreeNode =
    Json.decodeFromString(ViewTreeNode.serializer(), this)

fun FirstResponse.toJson():String =
    Json.encodeToString(this)

fun String.parseToFirstResponse():FirstResponse =
    Json.decodeFromString(this)

@Serializable
sealed class ClientSideEffect() {
    @Serializable
    data class OpenBrowser(val url:String):ClientSideEffect()
    @Serializable
    object OpenOrder:ClientSideEffect()
}

@Serializable
data class NetworkReducerResult(val state:ViewTreeNode, val sideEffects:List<ClientSideEffect>)

fun NetworkReducerResult.toJson():String =
    Json.encodeToString(this)

fun String.parseToNetworkReducerResult():NetworkReducerResult =
    Json.decodeFromString(this)

val SERVER_PATH_FIRST_REQUEST = "first_state"
val SERVER_PATH_NETWORK_REDUCER = "network_reducer"

@Serializable
data class FirstRequestBody(val userId: String, val clientStorage: ClientStorage)

fun FirstRequestBody.toJson():String =
    Json.encodeToString(this)

fun String.parseToFirstRequestBody():FirstRequestBody =
    Json.decodeFromString(this)

@Serializable
data class NetworkReducerRequestBody(
    val sessionId: String,
    val clientStorage: ClientStorage,
    val intent: Intent
)

fun NetworkReducerRequestBody.toJson():String =
    Json.encodeToString(this)

fun String.parseToNetworkReducerRequestBody():NetworkReducerRequestBody =
    Json.decodeFromString(this)

@Serializable
data class FirstResponse(val sessionId: String, val reducerResult: NetworkReducerResult)

@Serializable
data class ClientValue(val stringValue: String)

fun ClientStorage.toJson():String {
    return Json.encodeToString(this)
}

fun String.parseToClientStorage():ClientStorage {
    return Json.decodeFromString<ClientStorage>(this)
}

@Serializable
sealed class Intent {
    @Serializable
    data class ButtonPressed(val buttonId: Id) : Intent()
}

typealias ClientStorage = Map<String, ClientValue>
