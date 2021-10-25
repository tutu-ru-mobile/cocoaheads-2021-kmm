/*
 * Copyright 2020-2021 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

package example.imageviewer

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import example.imageviewer.model.Picture
import example.imageviewer.model.loadFullImage
import example.imageviewer.utils.toByteArray
import org.jetbrains.skia.Image

actual suspend fun downloadImageBitmap(url: String): ImageBitmap {
    val pic: Picture = loadFullImage(url)
        return Image.makeFromEncoded(toByteArray(pic.image)).toComposeImageBitmap()
}
