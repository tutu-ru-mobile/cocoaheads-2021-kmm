package ru.tutu


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO

@Composable
fun NetworkImage(imageUrl: String, width: Int, height: Int) {
    var mutableImage by remember(imageUrl) { mutableStateOf<ImageBitmap?>(null) }
    remember {
        APP_SCOPE.launch {
            mutableImage = downloadImageBitmap(imageUrl)
        }
    }
    val image = mutableImage
    if (image != null) {
        Image(
            BitmapPainter(image),
            contentDescription = null,
            modifier = Modifier.size(width.dp, height.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier.size(width.dp, height.dp).background(color = Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading image...")
        }
    }
}

private data class Picture(
    var source: String = "",
    var name: String = "",
    var image: BufferedImage,
    var width: Int = 0,
    var height: Int = 0,
    var id: Int = 0
)

private suspend fun downloadImageBitmap(url: String): ImageBitmap {
    val pic: Picture = loadFullImage(url)
    return org.jetbrains.skia.Image.makeFromEncoded(toByteArray(pic.image)).toComposeImageBitmap()
}

private fun loadFullImage(source: String): Picture {
    try {
        val url = URL(source)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.connect()

        val input: InputStream = connection.inputStream
        val bitmap: BufferedImage? = ImageIO.read(input)
        if (bitmap != null) {
            return Picture(
                source = source,
                image = bitmap,
                name = getNameURL(source),
                width = bitmap.width,
                height = bitmap.height
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return Picture(image = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB))
}

private fun getNameURL(url: String): String {
    return url.substring(url.lastIndexOf('/') + 1, url.length)
}

fun toByteArray(bitmap: BufferedImage) : ByteArray {
    val baos = ByteArrayOutputStream()
    ImageIO.write(bitmap, "png", baos)
    return baos.toByteArray()
}

fun getPreferredWindowSize(desiredWidth: Int, desiredHeight: Int): DpSize {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    val preferredWidth: Int = (screenSize.width * 0.8f).toInt()
    val preferredHeight: Int = (screenSize.height * 0.8f).toInt()
    val width: Int = if (desiredWidth < preferredWidth) desiredWidth else preferredWidth
    val height: Int = if (desiredHeight < preferredHeight) desiredHeight else preferredHeight
    return DpSize(width.dp, height.dp)
}
