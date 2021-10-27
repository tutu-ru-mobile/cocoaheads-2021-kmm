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
        "Server" to Random.nextDouble(),
        "Kotlin" to Random.nextDouble(),
    )
)

val pets = listOf(
    Pet(
        "Я кот",
        "https://media.istockphoto.com/photos/european-short-haired-cat-picture-id1072769156?k=20&m=1072769156&s=612x612&w=0&h=k6eFXtE7bpEmR2ns5p3qe_KYh098CVLMz4iKm5OuO6Y="
    ),
    Pet(
        "Я собака",
        "https://i.guim.co.uk/img/media/c63dddb413272fb6e8c308f0298c6333b3e2084f/0_139_4256_2554/master/4256.jpg?width=1200&height=1200&quality=85&auto=format&fit=crop&s=fed576179161a4ae8bf4dbe09ee40dc5"
    ),
    Pet(
        "Я енот",
        "https://kubnews.ru/upload/resize_cache/webp/iblock/7ea/7ea581116f09f65b820f7d5fe3c37eab.webp"
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
                height = 100
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
