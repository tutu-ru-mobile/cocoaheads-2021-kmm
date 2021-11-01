package ru.tutu

import ru.tutu.serialization.*

fun verticalContainer(backgroundColor:Color = Color(0x00_00_00_00u), lambda: NodeDsl.() -> Unit): ViewTreeNode = serverDrivenViewDsl {
    verticalContainer(backgroundColor = backgroundColor) {
        lambda()
    }
}.first()

@OptIn(ExperimentalStdlibApi::class)
private fun serverDrivenViewDsl(lambda: NodeDsl.() -> Unit): List<ViewTreeNode> {
    return buildList<ViewTreeNode> {
        object : NodeDsl {
            override fun verticalContainer(backgroundColor:Color, lambda: NodeDsl.() -> Unit) {
                add(ViewTreeNode.Container.Vertical(serverDrivenViewDsl(lambda), backgroundColor = backgroundColor))
            }

            override fun horizontalContainer(backgroundColor:Color, lambda: NodeDsl.() -> Unit) {
                add(ViewTreeNode.Container.Horizontal(serverDrivenViewDsl(lambda), backgroundColor = backgroundColor))
            }

            override fun button(id: Id, text: String, fontSize:Int) {
                add(ViewTreeNode.Leaf.Button(id, text, fontSize))
            }

            override fun input(hint: String, storageKey: String) {
                add(ViewTreeNode.Leaf.Input(hint, storageKey))
            }

            override fun text(text: String, fontSize:Int) {
                add(ViewTreeNode.Leaf.Text(text, fontSize))
            }

            override fun rectangle(width: Int, height: Int, color: Color) {
                add(ViewTreeNode.Leaf.Rectangle(color = color, width = width, height = height))
            }

            override fun image(imgUrl: String, width: Int, height: Int) {
                add(ViewTreeNode.Leaf.Image(imgUrl, width, height))
            }

            override fun space(size: Int) {
                add(ViewTreeNode.Leaf.Space(size))
            }
        }.lambda()
    }
}

interface NodeDsl {
    fun verticalContainer(backgroundColor:Color = Color(0x00_00_00_00u), lambda: NodeDsl.() -> Unit)
    fun horizontalContainer(backgroundColor:Color = Color(0x00_00_00_00u), lambda: NodeDsl.() -> Unit)
    fun button(id: Id, text: String, fontSize:Int = 20)
    fun input(hint: String, storageKey: String)
    fun text(text: String, fontSize:Int = 20)
    fun image(imgUrl: String, width: Int, height: Int)
    fun rectangle(width: Int, height: Int, color: Color)
    fun space(size: Int)
}
