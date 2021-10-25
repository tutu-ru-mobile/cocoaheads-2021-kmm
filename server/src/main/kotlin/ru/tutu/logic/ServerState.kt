package ru.tutu.logic

import ru.tutu.*
import ru.tutu.serialization.Id

/**
 * Внутренние представление State-а на сервере
 */
data class ServerState(
    val userId: String,
    val vaccineCode: String? = null,
    val pcrTestCode: String? = null,
    val screen: Screen = Screen.Info
)

sealed class Screen {
    object Info:Screen()
    object SetVaccine:Screen()
    object SetPcr:Screen()
}

val BUTTON_VACCINE = Id("button.screen_vaccine")
val BUTTON_PCR_TEST = Id("button.screen_pcr_test")
val BUTTON_SEND_VACCINE = Id("button.send_vaccine")
val BUTTON_SEND_PCR_TEST = Id("button.send_pcr_test")
val BUTTON_DELETE_COVID_DATA = Id("button.clear_covid_data")
val KEY_VACCINE = "key.vaccine"
val KEY_PCR_TEST = "key.pcr_test"
