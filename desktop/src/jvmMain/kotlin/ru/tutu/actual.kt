package ru.tutu

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.awt.image.BufferedImage

suspend fun downloadImageBitmap(url: String): ImageBitmap {
    val pic: Picture = loadFullImage(url)
        return Image.makeFromEncoded(toByteArray(pic.image)).toComposeImageBitmap()
}

data class Picture(
    var source: String = "",
    var name: String = "",
    var image: BufferedImage,
    var width: Int = 0,
    var height: Int = 0,
    var id: Int = 0
)
