package ru.tutu.logic

import ru.tutu.NodeDsl
import ru.tutu.serialization.*
import ru.tutu.verticalContainer
import kotlin.random.Random

data class Pet(
    val name:String,
    val img:String,
    val skills: Map<String, Double> = mapOf(
        "iOS" to Random.nextDouble(),
        "Android" to Random.nextDouble(),
        "Server" to Random.nextDouble()
    )
)

val pets = listOf(
    Pet(
        "Я Кот",
        "http://localhost:8081/static/cat.png"
    ),
    Pet(
        "Я Собака",
        "http://localhost:8081/static/dog.png"
    ),
    Pet(
        "Я Енот",
        "http://localhost:8081/static/racoon.png"
    )
)

fun serverRenderPlayground(clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer() {
        pets.forEach {
            petCard(it)
        }
    }
}

private fun NodeDsl.petCard(pet:Pet) {
    horizontalContainer(Color(0x220000ffu)) {
        verticalContainer() {
            label(pet.name)
            image(
                imgUrl = pet.img,
                width = 150,
                height = 150
            )
        }

        verticalContainer {
            horizontalContainer {
                pet.skills.forEach {
                    verticalContainer {
                        label(it.key)
                        val skill = (255 * it.value).toInt()
                        val green = skill
                        val red = 255 - skill
                        rectangle(50, 50, Color(red, green, 0))
                    }
                }
                repeat(4) {
                }
            }
            label("Детальная информация")
        }
    }

}

fun Color(red:Int, green:Int, blue:Int) = Color(0xFF000000u + ((red shl 16) + (green shl 8) + blue).toUInt())
