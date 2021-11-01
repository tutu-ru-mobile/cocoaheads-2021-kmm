package ru.tutu.logic

import ru.tutu.serialization.*
import ru.tutu.verticalContainer

fun serverRender(state: ServerState, clientStorage: ClientStorage) =
    verticalContainer {
        image(
            "http://localhost:8081/static/hay_stack.png",
            350, 300
        )
        text("Это ServerDrivenView")
    }

//language=Json
val resultStr = """
    {
      "type": "ru.tutu.serialization.ViewTreeNode.Container.Vertical",
      "children": [
        {
          "type": "ru.tutu.serialization.ViewTreeNode.Leaf.Rectangle",
          "width": 150,
          "height": 150,
          "color": {
            "hexValue": 855703296
          }
        },
        {
          "type": "ru.tutu.serialization.ViewTreeNode.Leaf.Text",
          "text": "Это ServerDrivenView",
          "fontSize": 20
        }
      ],
      "backgroundColor": {
        "hexValue": 0
      }
    }
"""

fun serverRenderCovid(state: ServerState, clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer {
        text("Новые требования для путешествий!")
        text("Необходима прививка, или ПЦР тест.")

        image(
            "http://localhost:8081/static/covid_test.png",
            250, 250
        )

        space(10)
        text("Пожалуйста внесите информацию:")
        button(id = BUTTON_VACCINE, text = "У меня есть сертификат вакцинации")
        button(id = BUTTON_PCR_TEST, text = "У меня есть ПЦР тест")

        space(20)
        text("Что делать, если нет нужных данных?")
        button(id = BUTTON_CANCEL_TRIP, text = "Отменить поездку")
        button(id = BUTTON_SUPPORT, text = "Связаться со службой поддержки")
        space(20)
    }
}

fun serverRenderWithButtons(state: ServerState, clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer {
        text("Новые требования для путешествий!")
        text("Необходима прививка, или ПЦР тест.")
        image(
            "http://localhost:8081/static/covid_test.png",
            250, 250
        )
        space(10)
        val vaccineCode = state.vaccineCode
        val pcrTestCode = state.pcrTestCode
        if (vaccineCode != null) {
            text("Ваш сертификат вакцинации: $vaccineCode")
            button(BUTTON_DELETE_COVID_DATA, "Удалить сертификат")
        } else if (pcrTestCode != null) {
            text("Ваш ПЦР тест № $pcrTestCode")
            button(BUTTON_DELETE_COVID_DATA, "Удалить тест")
        } else {
            when (state.screen) {
                is Screen.Info -> {
                    text("Пожалуйста внесите информацию:")
                    button(id = BUTTON_VACCINE, text = "У меня есть сертификат вакцинации")
                    button(id = BUTTON_PCR_TEST, text = "У меня есть ПЦР тест")
                }
                is Screen.SetVaccine -> {
                    text("Введите нормер сертификата вакцинации:")
                    horizontalContainer {
                        input("", KEY_VACCINE)
                        button(BUTTON_SEND_VACCINE, "Сохранить")
                    }
                }
                is Screen.SetPcr -> {
                    text("Введите нормер ПЦР теста:")
                    horizontalContainer {
                        input("", KEY_PCR_TEST)
                        button(BUTTON_SEND_PCR_TEST, "Сохранить")
                    }
                }
            }
        }

        space(20)
        text("Что делать, если нет нужных данных?")
        button(id = BUTTON_CANCEL_TRIP, text = "Отменить поездку")
        button(id = BUTTON_SUPPORT, text = "Связаться со службой поддержки")
        space(20)
    }
}
