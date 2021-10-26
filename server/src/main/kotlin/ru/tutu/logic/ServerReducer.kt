package ru.tutu.logic

import ru.tutu.serialization.*

class ServerReducerResult(val state: ServerState, val sideEffects: List<ClientSideEffect>)

fun serverReducer(state: ServerState, clientStorage: ClientStorage, intent: Intent): ServerReducerResult {
    val result = when (intent) {
        is Intent.ButtonPressed -> {
            when (intent.buttonId) {
                BUTTON_VACCINE -> {
                    state.copy(
                        screen = Screen.SetVaccine
                    )
                }
                BUTTON_PCR_TEST -> {
                    state.copy(
                        screen = Screen.SetPcr
                    )
                }
                BUTTON_SEND_VACCINE -> {
                    state.copy(
                        vaccineCode = clientStorage.map[KEY_VACCINE]?.stringValue,
                        screen = Screen.Info
                    )
                }
                BUTTON_SEND_PCR_TEST -> {
                    state.copy(
                        pcrTestCode = clientStorage.map[KEY_PCR_TEST]?.stringValue,
                        screen = Screen.Info
                    )
                }
                BUTTON_DELETE_COVID_DATA -> {
                    state.copy(
                        vaccineCode = null,
                        pcrTestCode = null
                    )
                }
                else -> {
                    state
                }
            }
        }
        is Intent.Init -> {
            state
        }
    }
    val sideEffects:List<ClientSideEffect> = when(intent) {
        is Intent.ButtonPressed -> {
            when(intent.buttonId) {
                BUTTON_SUPPORT -> {
                    listOf(ClientSideEffect.OpenSupportScreen())
                }
                BUTTON_CANCEL_TRIP ->{
                    listOf(ClientSideEffect.OpenOrder("Сервер послал SideEffect отмены заказа"))
                }
                else -> emptyList()
            }
        }
        else -> emptyList()
    }
    return ServerReducerResult(result, sideEffects)
}
