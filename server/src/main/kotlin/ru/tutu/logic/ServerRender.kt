package ru.tutu.logic

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
fun serverRender(state: ServerState, clientStorage: Map<String, ClientValue>): ViewTreeNode =
    verticalContainer {
        label("counter ${state.counter}")
        input("hint", KEY_INPUT1)
        label("Hello ${clientStorage[KEY_INPUT1]?.stringValue}")
        button(id = Id("button.send"), text = "send")
        horizontalContainer {
            rectangle(50, 50, 0xff00ff00u)
            rectangle(50, 50, 0xffffff00u)
            image(
                "https://raw.githubusercontent.com/JetBrains/compose-jb/master/artwork/imageviewerrepo/1.jpg",
                100,
                100
            )
        }
    }
