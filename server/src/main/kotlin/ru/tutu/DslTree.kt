package ru.tutu

fun verticalContainer(lambda: NodeDsl.() -> Unit): Node = refreshViewDsl {
    verticalContainer {
        lambda()
    }
}.first()

@OptIn(ExperimentalStdlibApi::class)
private fun refreshViewDsl(lambda: NodeDsl.() -> Unit): List<Node> {
    return buildList<Node> {
        object : NodeDsl {
            override fun verticalContainer(lambda: NodeDsl.() -> Unit) {
                add(Node.Container.V(refreshViewDsl(lambda)))
            }

            override fun horizontalContainer(lambda: NodeDsl.() -> Unit) {
                add(Node.Container.H(refreshViewDsl(lambda)))
            }

            override fun button(id: Id, text: String) {
                add(Node.Leaf.Button(id, text))
            }

            override fun input(hint: String, storageKey: String) {
                add(Node.Leaf.Input(hint, storageKey))
            }

            override fun label(text: String) {
                add(Node.Leaf.Label(text))
            }

            override fun rectangle(width: Int, height: Int, color: UInt) {
                add(Node.Leaf.Rectangle(color = color, width = width, height = height))
            }

            override fun image(imgUrl: String, width: Int, height: Int) {
                add(Node.Leaf.Image(imgUrl, width, height))
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
