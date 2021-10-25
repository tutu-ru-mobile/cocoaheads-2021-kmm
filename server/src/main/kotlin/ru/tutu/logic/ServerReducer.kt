package ru.tutu.logic

import ru.tutu.serialization.*

class ServerReducerResult(val state: ServerState, val sideEffects: List<ClientSideEffect>)

fun serverReducer(state: ServerState, clientStorage: ClientStorage, intent: Intent): ServerReducerResult {
    val result = when (intent) {
        is Intent.ButtonPressed -> {
            state.copy(
                counter = state.counter + 1
            )
        }
    }
    return ServerReducerResult(result, listOf())
}
