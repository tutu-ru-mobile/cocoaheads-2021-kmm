package ru.tutu

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmInline

val defaultStr = """{"type":"ru.tutu.Node.Container.V","children":[{"type":"ru.tutu.Node.Leaf.Label","text":"counter 3"},{"type":"ru.tutu.Node.Leaf.Input","hint":"hint","storageKey":"input1"},{"type":"ru.tutu.Node.Leaf.Label","text":"Hello null"},{"type":"ru.tutu.Node.Leaf.Button","id":"button.send","text":"send"},{"type":"ru.tutu.Node.Container.H","children":[{"type":"ru.tutu.Node.Leaf.Rectangle","width":50,"height":50,"color":4278255360},{"type":"ru.tutu.Node.Leaf.Rectangle","width":50,"height":50,"color":4294967040},{"type":"ru.tutu.Node.Leaf.Image","imgUrl":"https://raw.githubusercontent.com/JetBrains/compose-jb/master/artwork/imageviewerrepo/1.jpg","width":100,"height":100}]}]}"""
fun getDefaultNode():Node = defaultStr.parseToNode()

fun Node.toJson():String =
    Json.encodeToString(this)

fun String.parseToNode():Node =
    Json.decodeFromString(Node.serializer(), this)

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
data class ReducerResult2(val state:Node, val sideEffects:List<ClientSideEffect>)

fun ReducerResult2.toJson():String =
    Json.encodeToString(this)

fun String.parseToReducerResult():ReducerResult2 =
    Json.decodeFromString(this)

@Serializable
sealed class Node() {
    @Serializable
    sealed class Leaf() : Node() {
        @Serializable
        data class Rectangle(val width: Int, val height: Int, val color: UInt) : Leaf()

        @Serializable
        data class Label(val text: String) : Leaf()

        @Serializable
        data class Button(val id: Id, val text: String) : Leaf()

        @Serializable
        data class Input(val hint: String, val storageKey: String) : Leaf()

        @Serializable
        data class Image(val imgUrl:String, val width: Int, val height: Int):Leaf()
    }

    @Serializable
    sealed class Container() : Node() {
        abstract val children: List<Node>

        @Serializable
        class H(override val children: List<Node>) : Container()

        @Serializable
        class V(override val children: List<Node>) : Container()
    }
}

@JvmInline
@Serializable
value class Id(val value: String)

val SERVER_PATH_FIRST_REQUEST = "first_state"
val SERVER_PATH_NETWORK_REDUCER = "network_reducer"

@Serializable
data class FirstRequestBody(val userId: String, val clientStorage: Map<String, ClientValue>)

fun FirstRequestBody.toJson():String =
    Json.encodeToString(this)

fun String.parseToFirstRequestBody():FirstRequestBody =
    Json.decodeFromString(this)

@Serializable
data class NetworkReducerRequestBody(
    val sessionId: String,
    val clientStorage: Map<String, ClientValue>,
    val intent: Intent
)

fun NetworkReducerRequestBody.toJson():String =
    Json.encodeToString(this)

fun String.parseToNetworkReducerRequestBody():NetworkReducerRequestBody =
    Json.decodeFromString(this)

@Serializable
data class FirstResponse(val sessionId: String, val reducerResult: ReducerResult2)

@Serializable
data class ClientValue(val stringValue: String)

fun Map<String, ClientValue>.toJson():String {
    return Json.encodeToString(this)
}

fun String.parseToClientStorage():Map<String, ClientValue> {
    return Json.decodeFromString<Map<String, ClientValue>>(this)
}

@Serializable
sealed class Intent {
    @Serializable
    data class ButtonPressed(val buttonId: Id) : Intent()
}
