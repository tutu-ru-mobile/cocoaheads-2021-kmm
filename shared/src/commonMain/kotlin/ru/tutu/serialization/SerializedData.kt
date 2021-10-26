package ru.tutu.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.tutu.ReducerResult

fun ViewTreeNode.toJson(): String =
    Json.encodeToString(this)

fun String.parseToNode(): ViewTreeNode =
    Json.decodeFromString(ViewTreeNode.serializer(), this)

@Serializable
sealed class ClientSideEffect() {
    @Serializable
    data class OpenBrowser(val url: String) : ClientSideEffect()

    @Serializable
    data class OpenOrder(val additionInfo: String) : ClientSideEffect()

    @Serializable
    class OpenSupportScreen : ClientSideEffect()
}

@Serializable
data class SerializableReducerResult(val state: ViewTreeNode, val sideEffects: List<ClientSideEffect>)
typealias NetworkReducerResult = ReducerResult<ViewTreeNode, ClientSideEffect>

fun NetworkReducerResult.toJson(): String =
    Json.encodeToString(SerializableReducerResult(state, sideEffects))

fun String.parseToNetworkReducerResult(): NetworkReducerResult =
    Json.decodeFromString<SerializableReducerResult>(this).run {
        NetworkReducerResult(state, sideEffects)
    }

val SERVER_PATH_NETWORK_REDUCER = "network_reducer"

@Serializable
data class NetworkReducerRequestBody(
    val userId: String,
    val clientStorage: ClientStorage,
    val intent: Intent
)

fun NetworkReducerRequestBody.toJson(): String =
    Json.encodeToString(this)

fun String.parseToNetworkReducerRequestBody(): NetworkReducerRequestBody =
    Json.decodeFromString(this)

@Serializable
data class ClientValue(val stringValue: String)

@Serializable
sealed class Intent {
    @Serializable
    data class ButtonPressed(val buttonId: Id) : Intent()

    @Serializable
    object Init : Intent()
}

@Serializable
data class ClientStorage(
    val map: Map<String, ClientValue>
)
