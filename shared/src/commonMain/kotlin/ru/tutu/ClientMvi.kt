package ru.tutu

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.tutu.serialization.*

data class RefreshViewState(
    val clientStorage: ClientStorage,
    val serverData: ServerData = ServerData.Loading
) {
    sealed class ServerData {
        data class NetworkError(val exception:String) : ServerData()
        object Loading : ServerData()
        data class Loaded(val node: ViewTreeNode) : ServerData()
    }
}

sealed class ClientIntent() {
    class SendToServer(val intent: Intent) : ClientIntent()
    data class UpdateClientStorage(val key: String, val value: ClientValue) : ClientIntent()
    class UpdateState(val node: ViewTreeNode) : ClientIntent()
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
    ) { s, a: ClientIntent ->
        val serverData = s.serverData
        when (serverData) {
            is RefreshViewState.ServerData.Loaded -> {
                when (a) {
                    is ClientIntent.UpdateClientStorage -> {
                        s.copy(
                            clientStorage = s.clientStorage.copy(
                                map = s.clientStorage.map.toMutableMap().also {
                                    it[a.key] = a.value
                                }
                            )
                        ).withoutSideEffects()
                    }
                    is ClientIntent.SendToServer -> {
                        val result = networkReducer(userId, networkReducerUrl, s.clientStorage, a.intent)
                        val data = result.getOrNull()
                        if (data != null) {
                            val reducedNode: ViewTreeNode = data.state
                            s.copy(
                                serverData = serverData.copy(node = reducedNode)
                            ).withSideEffects(data.sideEffects)
                        } else {
                            s.copy(
                                serverData = RefreshViewState.ServerData.NetworkError(
                                    result.exceptionOrNull()?.stackTraceToString() ?: ""
                                )
                            ).withoutSideEffects()
                        }
                    }
                    is ClientIntent.UpdateState -> {
                        s.copy(
                            serverData = RefreshViewState.ServerData.Loaded(
                                node = a.node
                            )
                        ).withoutSideEffects()
                    }
                }
            }
            is RefreshViewState.ServerData.Loading -> {
                when (a) {
                    is ClientIntent.UpdateState -> {
                        s.copy(
                            serverData = RefreshViewState.ServerData.Loaded(
                                node = a.node
                            )
                        ).withoutSideEffects()
                    }
                    else -> throw Error("unpredictable state")
                }
            }
            is RefreshViewState.ServerData.NetworkError -> {
                when (a) {
                    is ClientIntent.UpdateState -> {
                        s.copy(
                            serverData = RefreshViewState.ServerData.Loaded(
                                a.node
                            )
                        ).withoutSideEffects()
                    }
                    else -> s.withoutSideEffects()
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
                    ClientIntent.UpdateState(
                        data.state
                    )
                )
            }
            delay(500)
        }
    }
    return resultStore
}
