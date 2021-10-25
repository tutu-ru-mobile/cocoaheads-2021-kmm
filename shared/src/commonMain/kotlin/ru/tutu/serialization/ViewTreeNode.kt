package ru.tutu.serialization

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Id(val value: String)

@Serializable
sealed class ViewTreeNode() {
    @Serializable
    sealed class Leaf() : ViewTreeNode() {
        @Serializable
        data class Rectangle(val width: Int, val height: Int, val color: UInt) : Leaf()

        @Serializable
        data class Label(val text: String) : Leaf()

        @Serializable
        data class Button(val id: Id, val text: String) : Leaf()

        @Serializable
        data class Input(val hint: String, val storageKey: String) : Leaf()

        @Serializable
        data class Image(val imgUrl:String, val width: Int, val height: Int, val scale:Float):Leaf()
    }

    @Serializable
    sealed class Container() : ViewTreeNode() {
        abstract val children: List<ViewTreeNode>

        @Serializable
        class Horizontal(override val children: List<ViewTreeNode>) : Container()

        @Serializable
        class Vertical(override val children: List<ViewTreeNode>) : Container()
    }
}

val ViewTreeNode.key:String get() = this.toString() //used in SwiftUI List key
