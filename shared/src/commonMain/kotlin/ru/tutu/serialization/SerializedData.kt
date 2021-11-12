package ru.tutu.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
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
    data class OpenOrder(val additionInfo: String?) : ClientSideEffect()

    @Serializable
    class OpenSupportScreen : ClientSideEffect()
}

@Serializable
data class SerializableReducerResult(val state: ViewTreeNode, val sideEffects: List<ClientSideEffect>)
typealias NetworkReducerResult = ReducerResult<ViewTreeNode, ClientSideEffect>

fun NetworkReducerResult.toJson(): String =
    Json.encodeToString(SerializableReducerResult(state, sideEffects))

fun NetworkReducerResult.toProtoBuff(): ByteArray =
    ProtoBuf.encodeToByteArray(SerializableReducerResult(state, sideEffects))

fun String.parseToNetworkReducerResult(): NetworkReducerResult =
    Json.decodeFromString<SerializableReducerResult>(this).run {
        NetworkReducerResult(state, sideEffects)
    }

fun ByteArray.parseToNetworkReducerResult(): NetworkReducerResult =
    ProtoBuf.decodeFromByteArray<SerializableReducerResult>(this).run {
        NetworkReducerResult(state, sideEffects)
    }

@Serializable
data class NetworkReducerRequestBody(
    val userId: String,
    val clientStorage: ClientStorage,
    val intent: Intent
)

fun NetworkReducerRequestBody.toJson(): String =
    Json.encodeToString(this)

fun NetworkReducerRequestBody.toProtoBuff(): ByteArray =
    ProtoBuf.encodeToByteArray(this)

fun String.parseToNetworkReducerRequestBody(): NetworkReducerRequestBody =
    Json.decodeFromString(this)

fun ByteArray.parseToNetworkReducerRequestBody(): NetworkReducerRequestBody =
    ProtoBuf.decodeFromByteArray(this)

@Serializable
data class ClientValue(val stringValue: String)

@Serializable
sealed class Intent {
    @Serializable
    data class ButtonPressed(val buttonId: Id) : Intent()

    @Serializable
    object UpdateView : Intent()
}

@Serializable
data class ClientStorage(
    val map: Map<String, ClientValue>
)
