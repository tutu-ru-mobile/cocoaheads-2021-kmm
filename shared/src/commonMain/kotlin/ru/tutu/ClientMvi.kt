package ru.tutu

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.tutu.serialization.*

data class ServerDrivenViewState(
    val clientStorage: ClientStorage,
    val screen: ServerDrivenViewScreen = ServerDrivenViewScreen.Loading
) {
    sealed class ServerDrivenViewScreen {
        data class NetworkError(val exception:String) : ServerDrivenViewScreen()
        object Loading : ServerDrivenViewScreen()
        data class Normal(val node: ViewTreeNode) : ServerDrivenViewScreen()
    }
}

sealed class ClientIntent() {
    class SendToServer(val serverIntent: Intent) : ClientIntent()
    data class UpdateClientStorage(val key: String, val value: ClientValue) : ClientIntent()
    internal class UpdateScreenState(val screen: ServerDrivenViewState.ServerDrivenViewScreen) : ClientIntent()
}

fun createServerDrivenViewStore(
    userId: String,
    networkReducerUrl: String,
    autoUpdate: Boolean,
    sideEffectHandler: (ClientSideEffect) -> Unit
): Store<ServerDrivenViewState, ClientIntent> {
    val mviStore = createStoreWithSideEffect<ServerDrivenViewState, ClientIntent, ClientSideEffect>(
        ServerDrivenViewState(clientStorage = ClientStorage(emptyMap())),
        effectHandler = { _, sideEffect -> sideEffectHandler(sideEffect) }
    ) { viewState: ServerDrivenViewState, intent: ClientIntent ->
        when (intent) {
            is ClientIntent.UpdateScreenState -> {
                val newState = viewState.copy(
                    screen = intent.screen
                )
                newState.noSideEffects()
            }
            is ClientIntent.UpdateClientStorage -> {
                viewState.copy(
                    clientStorage = viewState.clientStorage.copy(
                        map = viewState.clientStorage.map.toMutableMap().also {
                            it[intent.key] = intent.value
                        }
                    )
                ).noSideEffects()
            }
            is ClientIntent.SendToServer -> {
                val result = networkReducer(userId, networkReducerUrl, viewState.clientStorage, intent.serverIntent)
                val data = result.getOrNull()
                if (data != null) {
                    viewState.copy(
                        screen = ServerDrivenViewState.ServerDrivenViewScreen.Normal(data.state)
                    ).addSideEffects(data.sideEffects)
                } else {
                    viewState.copy(
                        screen = result.createErrorScreen()
                    ).noSideEffects()
                }
            }
        }
    }
    APP_SCOPE.launch {
        do {
            val result = networkReducer(userId, networkReducerUrl, mviStore.state.clientStorage, Intent.UpdateView)
            val data = result.getOrNull()
            if (data != null) {
                mviStore.send(
                    ClientIntent.UpdateScreenState(
                        ServerDrivenViewState.ServerDrivenViewScreen.Normal(data.state)
                    )
                )
            } else {
                mviStore.send(
                    ClientIntent.UpdateScreenState(result.createErrorScreen())
                )
            }
            delay(500)
        } while (autoUpdate)
    }

    return mviStore
}

fun Result<*>.createErrorScreen() =
    ServerDrivenViewState.ServerDrivenViewScreen.NetworkError(
        this.exceptionOrNull()?.stackTraceToString() ?: ""
    )
