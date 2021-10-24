import SwiftUI
import shared
import SerializedData

func greet() -> String {
    return Greeting().greeting()
}

let root = SerializedDataKt.getDefaultNode()

struct ContentView: View {
    var body: some View {
        VStack {
            RenderNode(node: root)
            Text(greet())
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

struct RenderNode: View {
    var node:Node

    var body: some View {
        if (node is Node.Container.ContainerV) {
            VStack {
                let v = node as! Node.Container.ContainerV

                Text("(root as! Node.Container.ContainerV).children.count")
                Text("\(v.children.count)")
            }
        } else if (node is Node.Leaf.LeafLabel) {
            let label = node as! Node.Leaf.LeafLabel
            Text(label.text)
        }
    }
}
