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
        case let containerVertical as ViewTreeNode.Container.ContainerVertical:
            ContainerVertical(containerVertical, clientStorage, sendIntent)
        case let containerHorizontal as ViewTreeNode.Container.ContainerHorizontal:
            ContainerHorizontal(containerHorizontal, clientStorage, sendIntent)
        case let text as ViewTreeNode.Leaf.LeafText:
            LeafText(text)
        case let rectangle as ViewTreeNode.Leaf.LeafRectangle:
            LeafRectangle(rectangle)
        case let button as ViewTreeNode.Leaf.LeafButton:
            LeafButton(button, clientStorage, sendIntent)
        case let img as ViewTreeNode.Leaf.LeafImage:
            LeafImage(img)
        case let input as ViewTreeNode.Leaf.LeafInput:
            LeafInput(input, clientStorage, sendIntent)
        case let spacer as ViewTreeNode.Leaf.LeafSpace:
            Spacer(minLength: CGFloat(spacer.size))
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
