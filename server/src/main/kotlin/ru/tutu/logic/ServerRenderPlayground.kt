package ru.tutu.logic

import ru.tutu.NodeDsl
import ru.tutu.serialization.*
import ru.tutu.verticalContainer
import kotlin.random.Random

val catUrl = "http://localhost:8081/static/cat.png"
val dogUrl = "http://localhost:8081/static/dog.png"
val racoonUrl = "http://localhost:8081/static/racoon.png"

val lightRed = Color(0x44_ff_00_u)
val lightGreen = Color(0x44_00_ff_00_u)
val lightBlue = Color(0x33_00_00_ff_u)
val lightPink = Color(0x44_ff_00_ffu)

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
    Pet("Cat", catUrl),
    Pet("Dog", dogUrl),
    Pet("Racoon", racoonUrl)
)

fun serverRenderPlayground(clientStorage: ClientStorage): ViewTreeNode {
    return verticalContainer() {
        pets.forEach {
            petCard(it)
        }
    }
}

private fun NodeDsl.petCard(pet:Pet) {
    horizontalContainer(lightBlue) {
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
