package ru.tutu.logic

import ru.tutu.NodeDsl
import ru.tutu.serialization.*
import ru.tutu.verticalContainer
import kotlin.random.Random

val moscowUrl = "http://localhost:8081/static/moscow.png"
val spbUrl = "http://localhost:8081/static/spb.png"
val vladivostokUrl = "http://localhost:8081/static/vladivostok.png"

val lightRed = Color(0x44_ff_00_00_u)
val lightGreen = Color(0x44_00_ff_00_u)
val lightBlue = Color(0x33_00_00_ff_u)
val lightPink = Color(0x44_ff_00_ffu)

data class CovidDay(
    val infected:Int,
    val day:String
)

data class City(
    val name:String,
    val img:String,
    val statistics:List<CovidDay> = List(7) {
        CovidDay(Random.nextInt(100, 1000), it.toDayOfWeek())
    },
    val description:String = createRandomDescription()
)

val cities = listOf(
    City("Москва", moscowUrl),
    City("Санкт\nПетербург", spbUrl),
    City("Владивосток", vladivostokUrl),
)

fun serverRenderPlayground(clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer() {
        cities.forEach {
            petCard(it)
        }
    }
}

fun NodeDsl.petCard(city:City) {
    horizontalContainer(backgroundColor = lightBlue) {
        verticalContainer {
            text(city.name)
            image(city.img, 120, 120)
        }
        verticalContainer {
            text("Количество заражений", fontSize = 18)
            horizontalContainer(contentAlignment = VAlign.Bottom) {
                city.statistics.forEach() {
                    val redComponent = (255 * it.infected/1000.0).toInt()
                    val greenComponent = 255 - redComponent
                    val height = it.infected/15
                    verticalContainer {
                        text(it.infected.toString(), fontSize = 10)
                        rectangle(20, height, Color(redComponent, greenComponent, 0))
                        text(it.day, fontSize = 14)
                    }
                }
            }
            text(city.description, fontSize = 14)
        }
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
