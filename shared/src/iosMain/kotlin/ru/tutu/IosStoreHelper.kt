package ru.tutu

import kotlinx.coroutines.flow.collectLatest
import ru.tutu.serialization.*

data class GlobalState(val str: String)

sealed class GlobalAction {
    class ActionInput(val str:String): GlobalAction()
}

class IosStoreHelper {
    val store = createRefreshViewStore()

    fun sendAction(a: ClientIntent) {
        store.send(a)
    }

    fun sendButtonPressedIntent(buttonId: Id) {
        store.send(ClientIntent.SendToServer(Intent.ButtonPressed(buttonId)))
    }

    fun sendUpdateClientStorageIntent(key: String, value: String) {
        store.send(ClientIntent.UpdateClientStorage(key, ClientValue(value)))
    }

    fun getClientStorageValue(key: String): String {
        return store.state.clientStorage[key]?.stringValue ?: ""
    }

    fun getLastState(): RefreshViewState {
        return store.stateFlow.value
    }

    fun convertUIntToInt(value: UInt) = value.toInt()

    fun addListener(listener: (RefreshViewState) -> Unit) {
        launchAppScope {
            store.stateFlow.collectLatest {
                listener(it)
            }
        }
    }
}
