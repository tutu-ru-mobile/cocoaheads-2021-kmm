package ru.tutu.logic

import ru.tutu.NodeDsl
import ru.tutu.serialization.*
import ru.tutu.verticalContainer
import kotlin.random.Random

val catUrl = "http://localhost:8081/static/cat.png"
val dogUrl = "http://localhost:8081/static/dog.png"
val racoonUrl = "http://localhost:8081/static/racoon.png"

val lightRed = Color(0x44_ff_00_00_u)
val lightGreen = Color(0x44_00_ff_00_u)
val lightBlue = Color(0x33_00_00_ff_u)
val lightPink = Color(0x44_ff_00_ffu)

val pets = listOf(
    Pet(
        "Cat",
        catUrl,
        mapOf(
            "iOS" to Random.nextDouble(),
            "Android" to Random.nextDouble(),
            "Server" to Random.nextDouble()
        )
    ),
    Pet(
        "Dog",
        dogUrl,
        mapOf(
            "iOS" to Random.nextDouble(),
            "Android" to Random.nextDouble(),
            "Server" to Random.nextDouble()
        )
    ),
    Pet(
        "Racoon",
        racoonUrl,
        mapOf(
            "iOS" to Random.nextDouble(),
            "Android" to Random.nextDouble(),
            "Server" to Random.nextDouble()
        )
    ),
)

fun serverRenderPlayground(clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer() {
        pets.forEach {
            renderPet(it)
        }
    }
}

data class Pet(
    val name: String,
    val imgUrl: String,
    val skills: Map<String, Double>
)

private fun NodeDsl.renderPet(pet: Pet) {
    horizontalContainer(backgroundColor = lightGreen) {
        verticalContainer {
            text("I am ${pet.name}")
            image(pet.imgUrl, 100, 100)
        }
        verticalContainer {
            horizontalContainer(backgroundColor = lightBlue) {
                pet.skills.forEach {
                    verticalContainer {
                        text(it.key)
                        val skillInt = (it.value * 255).toInt()
                        val red = 255 - skillInt
                        val green = skillInt
                        rectangle(50, 50, Color(red, green, 0))
                    }
                }
            }
            text("Additional info of Pet")
        }
    }
}