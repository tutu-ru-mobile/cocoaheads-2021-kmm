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
        switch node {
        case let verticalContainer as ViewTreeNode.Container.ContainerVertical:
            if (true) {
                VStack {
                    ForEach(verticalContainer.children) { child in
                        RenderNode(child, clientStorage, sendIntent)
                    }
                }
            } else {
                // Вертикальный контейнер можно было сделать с помощью List
                List(verticalContainer.children) { child in
                    RenderNode(child, clientStorage, sendIntent)
                }
            }
        case let horizontalContainer as ViewTreeNode.Container.ContainerHorizontal:
            HStack {
                ForEach(horizontalContainer.children) { child in
                    RenderNode(child, clientStorage, sendIntent)
                }
            }
        case let label as ViewTreeNode.Leaf.LeafLabel:
            Text(label.text)
        case let rectangle as ViewTreeNode.Leaf.LeafRectangle:
            LeafRectangle(rectangle)
        case let button as ViewTreeNode.Leaf.LeafButton:
            Button(action: {
                sendIntent(SwiftHelperKt.buttonIntent(buttonId: button.id))
            }) {
                Text(button.text)
            }
        case let img as ViewTreeNode.Leaf.LeafImage:
            AsyncImage(url: URL(string: img.imgUrl), scale: CGFloat(img.scale))
                    .frame(width: CGFloat(img.width), height: CGFloat(img.height))
        case let input as ViewTreeNode.Leaf.LeafInput:
            let value = clientStorage.getString(key: input.storageKey)
            RenderInputTextView(label: input.hint, value: value) { inputValueStr in
                sendIntent(SwiftHelperKt.updateClientStorageIntent(key: input.storageKey, value: inputValueStr))
            }
        default:
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
