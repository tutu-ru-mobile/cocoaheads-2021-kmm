package ru.tutu

import kotlinx.coroutines.launch
import ru.tutu.serialization.*

data class RefreshViewState(
    val clientStorage: ClientStorage,
    val serverData: ServerData = ServerData.Loading
) {
    sealed class ServerData {
        object Loading : ServerData()
        data class Loaded(val node: ViewTreeNode) : ServerData()
    }
}

sealed class ClientIntent() {
    class SendToServer(val intent: Intent):ClientIntent()
    data class UpdateClientStorage(val key: String, val value: ClientValue) : ClientIntent()
    class FirstServerResponse(val node:ViewTreeNode):ClientIntent()
}


fun createRefreshViewStore(userId: String, sideEffectHandler: (ClientSideEffect)->Unit): Store<RefreshViewState, ClientIntent> {
    val result = createStoreWithSideEffect<RefreshViewState, ClientIntent, ClientSideEffect>(
        RefreshViewState(
            clientStorage = ClientStorage(emptyMap())
        ),
        effectHandler = { store, sideEffect ->
            sideEffectHandler(sideEffect)
        }
    ) {s, a: ClientIntent ->
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
                        val networkReducerResult = networkReducer(userId, s.clientStorage, a.intent)
                        val reducedNode: ViewTreeNode = networkReducerResult.state
                        s.copy(
                            serverData = serverData.copy(node = reducedNode)
                        ).withSideEffects(networkReducerResult.sideEffects)
                    }
                    else -> throw Error("unpredictable state")
                }
            }
            is RefreshViewState.ServerData.Loading -> {
                when (a) {
                    is ClientIntent.FirstServerResponse -> {
                        s.copy(
                            serverData = RefreshViewState.ServerData.Loaded(
                                node = a.node
                            )
                        ).withoutSideEffects()
                    }
                    else -> throw Error("unpredictable state")
                }
            }
        }
    }
    APP_SCOPE.launch {
        val firstResponse = networkReducer(userId, result.state.clientStorage, Intent.Init)
        result.send(ClientIntent.FirstServerResponse(firstResponse.state))
    }
    return result
}
