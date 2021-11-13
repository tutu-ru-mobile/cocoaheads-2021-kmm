package ru.tutu.logic

import ru.tutu.NodeDsl
import ru.tutu.serialization.*
import ru.tutu.rootContainer

data class Day(
    val dayOfWeek:String,
    val infected:Int
)
data class City(
    val name:String,
    val img:String,
    val stat:List<Day> = List(7) {
        Day(
            it.toDayOfWeek(),
            (100..1000).random()
        )
    },
    val desc:String = createRandomDescription()
)


val cities = listOf(
    City("Москва", moscowUrl),
    City("Санкт\nПетербург", spbUrl),
    City("Владивосток", vladivostokUrl),
)

fun serverResponsePlayground(clientStorage: ClientStorage): ViewTreeNode {
    return rootContainer() {
        cities.forEach {
            cityCard(it)
        }
    }
}

fun NodeDsl.cityCard(city: City) {
    horizontalContainer(lightBlue) {
        verticalContainer {
            text(city.name)
            image(city.img, 100, 100)
        }
        verticalContainer {
            text("Статистика коронвируса", 15)
            horizontalContainer(contentAlignment = VAlign.Bottom) {
                city.stat.forEach {
                    verticalContainer {
                        text(it.infected.toString(), 14)
                        val normalized = it.infected / 1000.0
                        val red = (255 * normalized).toInt()
                        val green = 255 - red
                        val height = (60 * normalized).toInt()
                        rectangle(20, height, Color(red, green, 0))
                        text(it.dayOfWeek, 16)
                    }
                }
            }
            text(city.desc, 16)
        }

    }
}
