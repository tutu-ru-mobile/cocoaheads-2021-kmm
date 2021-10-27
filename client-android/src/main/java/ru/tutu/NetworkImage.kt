package ru.tutu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun NetworkImage(imageUrl: String, width: Int, height: Int) {
    var mutableImage by remember(imageUrl) { mutableStateOf<ImageBitmap?>(null) }
    remember {
        APP_SCOPE.launch {
            mutableImage = downloadImageBitmap(imageUrl.replace("localhost", "10.0.2.2"))
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

data class Picture(
    var source: String = "",
    var name: String = "",
    var image: Bitmap,
    var width: Int = 0,
    var height: Int = 0,
    var id: Int = 0
)

suspend fun downloadImageBitmap(url:String): ImageBitmap {
    return withContext(Dispatchers.IO) {
        loadFullImage(url).image.asImageBitmap()
    }
}

private fun loadFullImage(source: String): Picture {
    try {
        val url = URL(source)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.connect()

        val input: InputStream = connection.inputStream
        val bitmap: Bitmap? = BitmapFactory.decodeStream(input)
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

    return Picture(image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
}

private fun getNameURL(url: String): String {
    return url.substring(url.lastIndexOf('/') + 1, url.length)
}
