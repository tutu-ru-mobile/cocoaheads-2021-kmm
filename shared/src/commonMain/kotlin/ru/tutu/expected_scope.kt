package ru.tutu

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Для простоты примера я минимально использую корутины и эта функиця запускает корутины самым простым способом
 * В настоящем коде следует использовать CoroutineScope и Job-ы
 */

val APP_SCOPE by lazy { getAppScope() }

fun launchAppScope(block: suspend () -> Unit) {
    APP_SCOPE.launch {
        block()
    }
}

expect internal inline fun getAppScope():CoroutineScope
