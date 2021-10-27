package ru.tutu.logic

import ru.tutu.serialization.*
import ru.tutu.verticalContainer

fun serverRenderPlayground(clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer(Color(0x2200ff00u)) {
        horizontalContainer(Color(0x220000ffu)) {
            rectangle(50, 50, Color(0xffff0000u))
            rectangle(50, 50, Color(0xffff0000u))
            rectangle(50, 50, Color(0xffff0000u))
            rectangle(50, 50, Color(0xffff00ffu))
            rectangle(50, 50, Color(0xffff00ffu))
//            rectangle(50, 50, Color(0xffff00ffu))
        }

    }
}
