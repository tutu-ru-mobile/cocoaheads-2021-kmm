package ru.tutu

import kotlinx.coroutines.flow.collectLatest

data class GlobalState(val str: String)

sealed class GlobalAction {
    class ActionInput(val str:String): GlobalAction()
}

class IosStoreHelper {
    val store = createStore(GlobalState(str = "global state")) { s, a: GlobalAction ->
        when (a) {
            is GlobalAction.ActionInput -> {
                s.copy(str = a.str)
            } else -> {
            s
        }
        }
    }

    fun sendAction(a: GlobalAction) = store.send(a)

    val globalStateFlow = store.stateFlow
    fun getLastState() = globalStateFlow.value

    fun addListener(listener: (GlobalState) -> Unit) {
        launchAppScope {
            globalStateFlow.collectLatest {
                listener(it)
            }
        }
    }
}
