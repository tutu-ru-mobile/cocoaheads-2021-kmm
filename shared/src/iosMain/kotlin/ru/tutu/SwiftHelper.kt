package ru.tutu

import kotlinx.coroutines.flow.collectLatest
import platform.UIKit.UIColor
import ru.tutu.serialization.*
import kotlin.random.Random
import kotlin.random.nextUInt

class RefreshViewStoreWrapper(userId: String, networkReducerUrl:String, autoUpdate:Boolean, sideEffectHandler: (ClientSideEffect) -> Unit) {
    val store = createRefreshViewStore(userId, networkReducerUrl, autoUpdate = autoUpdate, sideEffectHandler)

    fun sendIntent(intent: ClientIntent) {
        store.send(intent)
    }

    fun getLastState(): RefreshViewState {
        return store.stateFlow.value
    }

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
