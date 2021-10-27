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
        "Cat",
        "http://localhost:8081/static/cat.png"
    ),
    Pet(
        "Dog",
        "http://localhost:8081/static/dog.png"
    ),
    Pet(
        "Racoon",
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
            label("I am ${pet.name}")
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
            label("Additional info")
        }
    }

}
