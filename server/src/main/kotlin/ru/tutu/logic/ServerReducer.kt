package ru.tutu.logic

import ru.tutu.ClientSideEffect
import ru.tutu.ClientValue
import ru.tutu.Intent

class ServerReducerResult(val state: ServerState, val sideEffects: List<ClientSideEffect>)

fun serverReducer(state: ServerState, clientStorage: Map<String, ClientValue>, intent: Intent): ServerReducerResult {
    val result = when (intent) {
        is Intent.ButtonPressed -> {
            state.copy(
                counter = state.counter + 1
            )
        }
    }
    return ServerReducerResult(result, listOf())
}
