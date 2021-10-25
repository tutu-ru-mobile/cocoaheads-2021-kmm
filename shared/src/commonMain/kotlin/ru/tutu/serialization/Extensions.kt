package ru.tutu.serialization

val ViewTreeNode.key: String get() = this.toString() //used in SwiftUI List key
val Color.alphaInt get():Int = ((hexValue shr 24) and 0xFFu).toInt()
val Color.redInt get():Int = ((hexValue shr 16) and 0xFFu).toInt()
val Color.greenInt get():Int = ((hexValue shr 8) and 0xFFu).toInt()
val Color.blueInt get():Int = ((hexValue shr 0) and 0xFFu).toInt()
val Color.alphaDouble get():Double = alphaInt / 255.0
val Color.redDouble get():Double = redInt / 255.0
val Color.greenDouble get():Double = greenInt / 255.0
val Color.blueDouble get():Double = blueInt / 255.0
