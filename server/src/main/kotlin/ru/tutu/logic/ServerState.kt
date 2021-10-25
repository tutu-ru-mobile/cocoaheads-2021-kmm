package ru.tutu.logic

import ru.tutu.*
import ru.tutu.serialization.Id

/**
 * Внутренние представление State-а на сервере
 */
data class ServerState(
    val userId: String,
    val counter: Int
)

val BUTTON_VACCINE = Id("button.send_vaccine")

