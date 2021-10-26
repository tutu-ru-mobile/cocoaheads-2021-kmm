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
    val resultStore = createStoreWithSideEffect<RefreshViewState, ClientIntent, ClientSideEffect>(
        RefreshViewState(
            clientStorage = ClientStorage(emptyMap())
        ),
        effectHandler = { store, sideEffect ->
            sideEffectHandler(sideEffect)
        }
    ) { oldViewState: RefreshViewState, intent: ClientIntent ->
        val screenState = oldViewState.screen
        when (screenState) {
            is RefreshViewState.RefreshViewScreen.Normal -> {
                when (intent) {
                    is ClientIntent.UpdateClientStorage -> {
                        oldViewState.copy(
                            clientStorage = oldViewState.clientStorage.copy(
                                map = oldViewState.clientStorage.map.toMutableMap().also {
                                    it[intent.key] = intent.value
                                }
                            )
                        ).withoutSideEffects()
                    }
                    is ClientIntent.SendToServer -> {
                        val result = networkReducer(userId, networkReducerUrl, oldViewState.clientStorage, intent.serverIntent)
                        val data = result.getOrNull()
                        if (data != null) {
                            val reducedNode: ViewTreeNode = data.state
                            oldViewState.copy(
                                screen = screenState.copy(node = reducedNode)
                            ).withSideEffects(data.sideEffects)
                        } else {
                            oldViewState.copy(
                                screen = RefreshViewState.RefreshViewScreen.NetworkError(
                                    result.exceptionOrNull()?.stackTraceToString() ?: ""
                                )
                            ).withoutSideEffects()
                        }
                    }
                    is ClientIntent.UpdateScreenState -> {
                        oldViewState.copy(
                            screen = intent.screen
                        ).withoutSideEffects()
                    }
                }
            }
            is RefreshViewState.RefreshViewScreen.Loading -> {
                when (intent) {
                    is ClientIntent.UpdateScreenState -> {
                        oldViewState.copy(
                            screen = intent.screen
                        ).withoutSideEffects()
                    }
                    else -> throw Error("unpredictable state")
                }
            }
            is RefreshViewState.RefreshViewScreen.NetworkError -> {
                when (intent) {
                    is ClientIntent.UpdateScreenState -> {
                        oldViewState.copy(
                            screen = intent.screen
                        ).withoutSideEffects()
                    }
                    else -> oldViewState.withoutSideEffects()
                }
            }

        }
    }
    APP_SCOPE.launch {
        repeat(if (autoUpdate) Int.MAX_VALUE else 1) {
            val result = networkReducer(userId, networkReducerUrl, resultStore.state.clientStorage, Intent.UpdateView)
            val data = result.getOrNull()
            if (data != null) {
                resultStore.send(
                    ClientIntent.UpdateScreenState(
                        RefreshViewState.RefreshViewScreen.Normal(data.state)
                    )
                )
            } else {
                resultStore.send(
                    ClientIntent.UpdateScreenState(
                        RefreshViewState.RefreshViewScreen.NetworkError(
                            result.exceptionOrNull()?.stackTraceToString() ?: ""
                        )
                    )
                )
            }
            delay(500)
        }
    }
    return resultStore
}
