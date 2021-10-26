package ru.tutu

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

typealias Reducer<STATE, INTENT> = suspend (STATE, INTENT) -> STATE

interface Store<STATE, INTENT> {
    fun send(intent: INTENT)
    val stateFlow: StateFlow<STATE>
    val state get() = stateFlow.value
}
/**
 * Самая простая реализация MVI архитектуры для слоя представления.
 */
fun <STATE, INTENT> createStore(init: STATE, reducer: Reducer<STATE, INTENT>): Store<STATE, INTENT> {
    val mutableStateFlow = MutableStateFlow(init)
    val channel: Channel<INTENT> = Channel(Channel.UNLIMITED)

    return object : Store<STATE, INTENT> {
        init {
            //https://m.habr.com/ru/company/kaspersky/blog/513364/
            //or alternative in jvm use fun CoroutineScope.actor(...)
            APP_SCOPE.launch {
                channel.consumeAsFlow().collect { intent ->
                    mutableStateFlow.value = reducer(mutableStateFlow.value, intent)
                }
            }
        }

        override fun send(intent: INTENT) {
            channel.offer(intent)//mutableStateFlow.value = reducer(mutableStateFlow.value, intent)
        }

        override val stateFlow: StateFlow<STATE> = mutableStateFlow
    }
}

typealias ReducerSE<STATE, INTENT, EFFECT> = suspend (STATE, INTENT) -> ReducerResult<STATE, EFFECT>

class ReducerResult<STATE, EFFECT>(val state: STATE, val sideEffects: List<EFFECT> = emptyList())

/**
 * MVI по типу ELM с обработкой SideEffect-ов
 */
fun <STATE, INTENT, EFFECT> createStoreWithSideEffect(
    init: STATE,
    effectHandler: (store: Store<STATE, INTENT>, sideEffect: EFFECT) -> Unit,
    reducer: ReducerSE<STATE, INTENT, EFFECT>
): Store<STATE, INTENT> {
    lateinit var store: Store<STATE, INTENT>
    store = createStore(init) { state, intent ->
        val result = reducer(state, intent)

        result.sideEffects.forEach {
            effectHandler(store, it)
        }

        result.state
    }
    return store
}

fun <STATE:Any, EFFECT> STATE.noSideEffects() = ReducerResult(this, emptyList<EFFECT>())
fun <STATE:Any, EFFECT> STATE.addSideEffects(sideEffects: List<EFFECT>) = ReducerResult(this, sideEffects)
