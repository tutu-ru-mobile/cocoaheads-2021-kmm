package ru.tutu

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.tutu.logic.ServerState
import ru.tutu.logic.serverReducer
import ru.tutu.logic.serverRender
import java.util.concurrent.ConcurrentHashMap
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
        post("/important_reducer") {
            val clientData = call.receiveText().parseToNetworkReducerRequestBody()
            val result = serverNetworkReducer(clientData.userId, clientData.clientStorage, clientData.intent)
            call.respondText(
                text = result.toJson(),
                contentType = ContentType.Application.Json
            )
        }
        post("/playground_reducer") {
            val clientData = call.receiveText().parseToNetworkReducerRequestBody()
            val result = serverNetworkReducer(clientData.userId, clientData.clientStorage, clientData.intent)
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

private val mapUserIdToServerState: MutableMap<String, ServerState> = ConcurrentHashMap()

private suspend fun serverNetworkReducer(
    userId: String,
    clientStorage: ClientStorage,
    intent: Intent
): NetworkReducerResult {
    val state: ServerState = mapUserIdToServerState.getOrPut(userId) {
        ServerState(userId)
    }
    val reducerResult = serverReducer(state, clientStorage, intent)
    mapUserIdToServerState[userId] = reducerResult.state
    val node = serverRender(reducerResult.state, clientStorage)
    return NetworkReducerResult(node.toJson().parseToNode(), reducerResult.sideEffects)
}
