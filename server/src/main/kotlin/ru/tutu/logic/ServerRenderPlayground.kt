package ru.tutu.logic

import ru.tutu.serialization.*
import ru.tutu.verticalContainer
import ru.tutu.NodeDsl
import ru.tutu.serialization.*
import ru.tutu.verticalContainer
import kotlin.random.Random

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

fun serverResponsePlayground(clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer() {
//        rectangle(100, 100, lightGreen)
    }
//    return verticalContainer() {
//        cities.forEach {
//            petCard(it)
//        }
//    }
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
