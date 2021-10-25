import SwiftUI
import Shared

struct RenderNode: View {
    let node: NSObject
    let clientStorage: ClientStorage
    let sendIntent: (ClientIntent) -> Void

    init(_ node: NSObject, _ clientStorage: ClientStorage, _ sendIntent: @escaping (ClientIntent) -> Void) {
        self.node = node
        self.clientStorage = clientStorage
        self.sendIntent = sendIntent
    }

    var body: some View {
        if (node is ViewTreeNode.Container.ContainerVertical) {
            let v = node as! ViewTreeNode.Container.ContainerVertical
            if (true) {
                VStack {
                    ForEach(v.children) { child in
                        RenderNode(child, clientStorage, sendIntent)
                    }
                }
            } else {
                // Вертикальный контейнер можно было сделать с помощью List
                List(v.children) { child in
                    RenderNode(child, clientStorage, sendIntent)
                }
            }
        } else if (node is ViewTreeNode.Container.ContainerHorizontal) {
            let v = node as! ViewTreeNode.Container.ContainerHorizontal
            HStack {
                ForEach(v.children) { child in
                    RenderNode(child, clientStorage, sendIntent)
                }
            }
        } else if (node is ViewTreeNode.Leaf.LeafLabel) {
            let label = node as! ViewTreeNode.Leaf.LeafLabel
            Text(label.text)
        } else if (node is ViewTreeNode.Leaf.LeafRectangle) {
            LeafRectangle(node as! ViewTreeNode.Leaf.LeafRectangle)
        } else if (node is ViewTreeNode.Leaf.LeafButton) {
            let button = node as! ViewTreeNode.Leaf.LeafButton
            Button(action: {
                sendIntent(SwiftHelperKt.buttonIntent(buttonId: button.id))
            }) {
                Text(button.text)
            }
        } else if (node is ViewTreeNode.Leaf.LeafImage) {
            let img = node as! ViewTreeNode.Leaf.LeafImage
            AsyncImage(url: URL(string: img.imgUrl), scale: CGFloat(img.scale))
                    .frame(width: CGFloat(img.width), height: CGFloat(img.height))
        } else if (node is ViewTreeNode.Leaf.LeafInput) {
            let input = node as! ViewTreeNode.Leaf.LeafInput
            let value = clientStorage.getString(key: input.storageKey)
            RenderInputTextView(label: input.hint, value: value) { inputValueStr in
                sendIntent(SwiftHelperKt.updateClientStorageIntent(key: input.storageKey, value: inputValueStr))
            }
        } else {
            Text("unknown node type, node: \(node)")
        }
    }
}

extension ViewTreeNode: Identifiable {
    public var id: String {
        get {
            key
        }
    }
}

