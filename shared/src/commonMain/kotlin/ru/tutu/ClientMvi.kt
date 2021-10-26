package ru.tutu

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.tutu.serialization.*

data class RefreshViewState(
    val clientStorage: ClientStorage,
    val screen: RefreshViewScreen = RefreshViewScreen.Loading
) {
    sealed class RefreshViewScreen {
        data class NetworkError(val exception:String) : RefreshViewScreen()
        object Loading : RefreshViewScreen()
        data class Normal(val node: ViewTreeNode) : RefreshViewScreen()
    }
}

sealed class ClientIntent() {
    class SendToServer(val serverIntent: Intent) : ClientIntent()
    data class UpdateClientStorage(val key: String, val value: ClientValue) : ClientIntent()
    internal class UpdateScreenState(val screen: RefreshViewState.RefreshViewScreen) : ClientIntent()
}

fun createRefreshViewStore(
    userId: String,
    networkReducerUrl: String,
    autoUpdate: Boolean,
    sideEffectHandler: (ClientSideEffect) -> Unit
): Store<RefreshViewState, ClientIntent> {
    val mviStore = createStoreWithSideEffect<RefreshViewState, ClientIntent, ClientSideEffect>(
        RefreshViewState(clientStorage = ClientStorage(emptyMap())),
        effectHandler = { _, sideEffect -> sideEffectHandler(sideEffect) }
    ) { viewState: RefreshViewState, intent: ClientIntent ->
        when (intent) {
            is ClientIntent.UpdateScreenState -> {
                viewState.copy(
                    screen = intent.screen
                ).noSideEffects()
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
                        screen = RefreshViewState.RefreshViewScreen.Normal(data.state)
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
                        RefreshViewState.RefreshViewScreen.Normal(data.state)
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
    RefreshViewState.RefreshViewScreen.NetworkError(
        this.exceptionOrNull()?.stackTraceToString() ?: ""
    )
