package ru.tutu

import kotlinx.coroutines.launch

data class RefreshViewState(
    val clientStorage: Map<String, ClientValue>,
    val serverData: ServerData = ServerData.Loading
) {
    sealed class ServerData {
        object Loading : ServerData()
        data class Loaded(val sessionId: String, val node: ViewTreeNode) : ServerData()
    }
}

sealed class ClientIntent() {
    class SendToServer(val intent: Intent):ClientIntent()
    data class UpdateClientStorage(val key: String, val value: ClientValue) : ClientIntent()
    class FirstServerResponse(val node:ViewTreeNode, val sessionId: String):ClientIntent()
}


fun createRefreshViewStore(): Store<RefreshViewState, ClientIntent> {
    val result = createStore(
        RefreshViewState(
            clientStorage = emptyMap()
        )
    ) {s, a: ClientIntent ->
        val serverData = s.serverData
        when (serverData) {
            is RefreshViewState.ServerData.Loaded -> {
                when (a) {
                    is ClientIntent.UpdateClientStorage -> {
                        s.copy(
                            clientStorage = s.clientStorage.toMutableMap().also {
                                it[a.key] = a.value
                            }
                        )
                        //todo изменения отправлять на сервер
                    }
                    is ClientIntent.SendToServer -> {
                        val reducedNode: ViewTreeNode = networkReducer(serverData.sessionId, s.clientStorage, a.intent)
                        s.copy(
                            serverData = serverData.copy(node = reducedNode)
                        )
                    }
                    else -> throw Error("unpredictable state")
                }
            }
            is RefreshViewState.ServerData.Loading -> {
                when (a) {
                    is ClientIntent.FirstServerResponse -> {
                        s.copy(
                            serverData = RefreshViewState.ServerData.Loaded(
                                sessionId = a.sessionId,
                                node = a.node
                            )
                        )
                    }
                    else -> throw Error("unpredictable state")
                }
            }
        }
    }
    APP_SCOPE.launch {
        val firstResponse = getFirstState("my UID", result.state.clientStorage)
        result.send(ClientIntent.FirstServerResponse(firstResponse.reducerResult.state, firstResponse.sessionId))
    }
    return result
}
