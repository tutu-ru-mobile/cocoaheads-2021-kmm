package ru.tutu.logic

import ru.tutu.serialization.*
import ru.tutu.verticalContainer

fun serverRenderPlayground(clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer {
        horizontalContainer {
            rectangle(50, 50, Color(0xffff0000u))
            rectangle(50, 50, Color(0xffff0000u))
            rectangle(50, 50, Color(0xffff0000u))
        }

    }
}
