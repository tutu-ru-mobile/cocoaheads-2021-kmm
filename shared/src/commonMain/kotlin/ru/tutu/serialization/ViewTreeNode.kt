package ru.tutu.serialization

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class Id(val value: String)

@Serializable
data class Color(val hexValue: UInt)

@Serializable
sealed class VAlign {
    @Serializable
    object Top : VAlign()
    @Serializable
    object Center : VAlign()
    @Serializable
    object Bottom : VAlign()
}

@Serializable
sealed class HAlign {
    @Serializable
    object Left : HAlign()
    @Serializable
    object Center : HAlign()
    @Serializable
    object Right : HAlign()
}

@Serializable
sealed class ViewTreeNode() {
    @Serializable
    sealed class Leaf() : ViewTreeNode() {
        @Serializable
        data class Rectangle(val width: Int, val height: Int, val color: Color) : Leaf()

        @Serializable
        data class Text(val text: String, val fontSize: Int) : Leaf()

        @Serializable
        data class Button(val id: Id, val text: String, val fontSize:Int) : Leaf()

        @Serializable
        data class Input(val hint: String, val storageKey: String) : Leaf()

        @Serializable
        data class Image(val imgUrl: String, val width: Int, val height: Int) : Leaf()

        @Serializable
        data class Space(val size: Int) : ViewTreeNode()
    }

    @Serializable
    sealed class Container() : ViewTreeNode() {
        abstract val children: List<ViewTreeNode>

        @Serializable
        data class Horizontal(
            override val children: List<ViewTreeNode>,
            val backgroundColor: Color,
            val verticalAlignment: VAlign
        ) : Container()

        @Serializable
        data class Vertical(
            override val children: List<ViewTreeNode>,
            val backgroundColor: Color,
            val horizontalAlignment: HAlign
        ) : Container()
    }
}

fun Color(red:Int, green:Int, blue:Int, alpha:Int = 0xff) = Color( ((alpha shl 24) + (red shl 16) + (green shl 8) + blue).toUInt())
