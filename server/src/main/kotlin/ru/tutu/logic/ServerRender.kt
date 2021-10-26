package ru.tutu.logic

import ru.tutu.serialization.*
import ru.tutu.verticalContainer

fun serverRender(state: ServerState, clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer {
        label("Новые требования для путешествий!")
        label("Необходима прививка, или ПЦР тест.")
        image(
            "http://localhost:8081/static/covid_test.png",
            250, 250
        )
        space(20)
        val vaccineCode = state.vaccineCode
        val pcrTestCode = state.pcrTestCode
        if (vaccineCode != null) {
            label("Ваш сертификат вакцинации: $vaccineCode")
            button(BUTTON_DELETE_COVID_DATA, "Удалить эту информацию обо мне")
        } else if (pcrTestCode != null) {
            label("Ваш ПЦР тест № $pcrTestCode")
            button(BUTTON_DELETE_COVID_DATA, "Удалить эту информацию обо мне")
        } else {
            when (state.screen) {
                is Screen.Info -> {
                    label("Пожалуйста внесите информацию:")
                    button(id = BUTTON_VACCINE, text = "У меня есть сертификат вакцинации")
                    button(id = BUTTON_PCR_TEST, text = "У меня есть ПЦР тест")
                }
                is Screen.SetVaccine -> {
                    label("Введите нормер сертификата вакцинации:")
                    horizontalContainer {
                        input("", KEY_VACCINE)
                        button(BUTTON_SEND_VACCINE, "Сохранить")
                    }
                }
                is Screen.SetPcr -> {
                    label("Введите нормер ПЦР теста:")
                    horizontalContainer {
                        input("", KEY_PCR_TEST)
                        button(BUTTON_SEND_PCR_TEST, "Сохранить")
                    }
                }
            }
        }

        space(20)
        label("Что делать, если нет нужных данных?")
        button(id = BUTTON_CANCEL_TRIP, text = "Отменить поездку")
        button(id = Id("button.covid_faq"), text = "Частые вопросы в связи с коронавирусом")

        space(40)
        label("Нужна дополнительная помощь?")
        button(id = BUTTON_SUPPORT, text = "Связаться со службой поддержки")
        space(20)
    }
}
