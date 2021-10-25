package ru.tutu

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.delay
import ru.tutu.logic.ServerState
import ru.tutu.logic.serverReducer
import ru.tutu.logic.serverRender
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random
import ru.tutu.serialization.*

fun main() {
    configureServer().start(wait = true)
}

fun startServer(): AutoCloseable {
    val server = configureServer().start(wait = false)
    return AutoCloseable {
        server.stop(200, 400)
    }
}

private fun configureServer() =
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", watchPaths = listOf("classes", "resources")) {
        configureRouting()
    }

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World 5!")
        }
        post(SERVER_PATH_FIRST_REQUEST) {
            val clientData = call.receiveText().parseToFirstRequestBody()
            val result: FirstResponse = firstRequest(clientData.userId, clientData.clientStorage)
            call.respondText(
                text = result.toJson(),
                contentType = ContentType.Application.Json
            )
        }
        post(SERVER_PATH_NETWORK_REDUCER) {
            val clientData = call.receiveText().parseToNetworkReducerRequestBody()
            val result = networkReducer(clientData.sessionId, clientData.clientStorage, clientData.intent)
            call.respondText(
                text = result.toJson(),
                contentType = ContentType.Application.Json
            )
        }
        static("/static") {
            resources("static")
        }
    }
}

private val mapSessionToServerState: MutableMap<String, ServerState> = ConcurrentHashMap()

private suspend fun firstRequest(userId: String, clientStorage: Map<String, ClientValue>): FirstResponse {
    delay(300)
    val session = Random.nextInt().toString()
    val state = ServerState(userId, 0)
    mapSessionToServerState[session] = state
    return FirstResponse(session, NetworkReducerResult(serverRender(state, clientStorage), listOf()))
}

private suspend fun networkReducer(
    sessionId: String,
    clientStorage: Map<String, ClientValue>,
    intent: Intent
): NetworkReducerResult {
    val state: ServerState = mapSessionToServerState[sessionId]
        ?: return NetworkReducerResult(ViewTreeNode.Leaf.Label("Session not found. Please restart Application"), listOf())

    val reducerResult = serverReducer(state, clientStorage, intent)
    mapSessionToServerState[sessionId] = reducerResult.state
    val node = serverRender(reducerResult.state, clientStorage)
    return NetworkReducerResult(node.toJson().parseToNode(), reducerResult.sideEffects)
}
