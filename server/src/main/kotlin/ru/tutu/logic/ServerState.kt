package ru.tutu.logic

import ru.tutu.*

/**
 * Внутренние представление State-а на сервере
 */
data class ServerState(
    val userId: String,
    val counter: Int
)

val KEY_INPUT1 = "input1"
