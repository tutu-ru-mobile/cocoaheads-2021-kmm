package ru.tutu

import kotlinx.coroutines.flow.collectLatest
import platform.UIKit.UIColor
import ru.tutu.serialization.*
import kotlin.random.Random
import kotlin.random.nextUInt

data class GlobalState(val str: String)

sealed class GlobalAction {
    class ActionInput(val str:String): GlobalAction()
}

class SwiftStoreHelper(userId: String, networkReducerUrl:String, sideEffectHandler: (ClientSideEffect) -> Unit) {
    val store = createRefreshViewStore(userId, networkReducerUrl, sideEffectHandler)

    fun sendAction(a: ClientIntent) {
        store.send(a)
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

fun Color.toUIColor():UIColor {
    return UIColor(red = redDouble, green = greenDouble, blue = blueDouble, alpha = alphaDouble)
}

fun buttonIntent(buttonId: Id): ClientIntent {
    return ClientIntent.SendToServer(Intent.ButtonPressed(buttonId))
}

fun updateClientStorageIntent(key:String, value: String):ClientIntent =
    ClientIntent.UpdateClientStorage(key, ClientValue(value))

fun ClientStorage.getString(key: String):String {
    return map[key]?.stringValue ?: ""
}
