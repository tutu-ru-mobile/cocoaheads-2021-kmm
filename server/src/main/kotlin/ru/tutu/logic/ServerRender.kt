package ru.tutu.logic

import ru.tutu.SERVER_URL
import ru.tutu.serialization.*
import ru.tutu.verticalContainer

/**
 * Внимание новые короновирусные ограничения
 * Сохраняйте меры предосторожности. КАРТИНКА
 * Пожалуйста введите номер сертификата вакцины (если имеется)
 * Проверить отменённый рейс можно в списке заказов LINK
 * Если возникнут вопросы - пишите в чат поддержки LINK
 * Частые вопросы в связи с коронавирусом (доп. текст)
 */
fun serverRender(state: ServerState, clientStorage: ClientStorage): ViewTreeNode =
    verticalContainer {
        label("Новые требования для путешествий.")
        label("Необходима прививка, или ПЦР тест.")
        image(
            "$SERVER_URL/static/covid_test.png",
            140, 140, 2.4f
        )
        space(20)
        label("Пожалуйста внесите информацию:")
        button(id = BUTTON_VACCINE, text = "У меня есть сертификат вакцинации")
        button(id = Id("button.send_pcr_test"), text = "У меня есть ПЦР тест")

        space(20)
        label("Что делать, если нет нужных данных?")
        button(id = Id("button.cancel_trip"), text = "Отменить поездку")
        button(id = Id("button.covid_faq"), text = "Частые вопросы в связи с коронавирусом")

        space(40)
        label("Нужна дополнительная помощь?")
        button(id = Id("button.support"), text = "Связаться со службой поддержки")
//        label("counter ${state.counter}")
//        input("hint", KEY_INPUT1)
//        label("Hello ${clientStorage.map[KEY_INPUT1]?.stringValue}")
//        button(id = Id("button.send"), text = "send")
//        horizontalContainer {
//            rectangle(50, 50, Color(0xff00ff00u))
//            rectangle(50, 50, Color(0xffffff00u))
//        }
    }
