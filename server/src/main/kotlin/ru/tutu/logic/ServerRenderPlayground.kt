package ru.tutu.logic

import ru.tutu.serialization.*
import ru.tutu.verticalContainer


fun serverResponsePlayground(clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer() {
//        rectangle(100, 100, lightGreen)
        text("Изменённый текст")
        image(spbUrl, 200, 200)
        button(Id("asdfg"), "Кнопка")
    }
}






























fun Int.toDayOfWeek():String =
    when(this) {
        0 -> "Пн"
        1 -> "Вт"
        2 -> "Ср"
        3 -> "Чт"
        4 -> "Пт"
        5 -> "Сб"
        6 -> "Вс"
        else -> ".."
    }

fun createRandomDescription() = listOf(
    "Кафе и рестораны работают только на вынос",
    "Для проезда в общественном транспорте надевайте перчатки",
    "Закрыты торговые и развлекательные центры",
    "Кафе и рестораны работают c 10:00 до 20:00",
    "Для приобритения Авиа билетов потребуется QR код",
).random()

val moscowUrl = "http://localhost:8081/static/moscow.png"
val spbUrl = "http://localhost:8081/static/spb.png"
val vladivostokUrl = "http://localhost:8081/static/vladivostok.png"

val lightRed = Color(0x44_ff_00_00_u)
val lightGreen = Color(0x44_00_ff_00_u)
val lightBlue = Color(0x33_00_00_ff_u)
val lightPink = Color(0x44_ff_00_ffu)
