package ru.tutu

import ru.tutu.serialization.*

fun verticalContainer(lambda: NodeDsl.() -> Unit): ViewTreeNode = refreshViewDsl {
    verticalContainer {
        lambda()
    }
}.first()

@OptIn(ExperimentalStdlibApi::class)
private fun refreshViewDsl(lambda: NodeDsl.() -> Unit): List<ViewTreeNode> {
    return buildList<ViewTreeNode> {
        object : NodeDsl {
            override fun verticalContainer(lambda: NodeDsl.() -> Unit) {
                add(ViewTreeNode.Container.Vertical(refreshViewDsl(lambda)))
            }

            override fun horizontalContainer(lambda: NodeDsl.() -> Unit) {
                add(ViewTreeNode.Container.Horizontal(refreshViewDsl(lambda)))
            }

            override fun button(id: Id, text: String) {
                add(ViewTreeNode.Leaf.Button(id, text))
            }

            override fun input(hint: String, storageKey: String) {
                add(ViewTreeNode.Leaf.Input(hint, storageKey))
            }

            override fun label(text: String) {
                add(ViewTreeNode.Leaf.Label(text))
            }

            override fun rectangle(width: Int, height: Int, color: UInt) {
                add(ViewTreeNode.Leaf.Rectangle(color = color, width = width, height = height))
            }

            override fun image(imgUrl: String, width: Int, height: Int) {
                add(ViewTreeNode.Leaf.Image(imgUrl, width, height))
            }
        }.lambda()
    }
}

interface NodeDsl {
    fun verticalContainer(lambda: NodeDsl.() -> Unit)
    fun horizontalContainer(lambda: NodeDsl.() -> Unit)
    fun button(id: Id, text: String)
    fun input(hint: String, storageKey: String)
    fun label(text: String)
    fun image(imgUrl: String, width: Int, height: Int)
    fun rectangle(width: Int, height: Int, color: UInt)
}
