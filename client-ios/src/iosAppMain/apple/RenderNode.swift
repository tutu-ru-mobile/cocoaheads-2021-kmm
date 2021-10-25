import SwiftUI
import ClientShared

struct RenderNode: View {
    var node:NSObject
    var di:IosStoreHelper

    var body: some View {
        if (node is ViewTreeNode.Container.ContainerVertical) {
            let v = node as! ViewTreeNode.Container.ContainerVertical
            List(v.children, id: \.key, rowContent: { data in RenderNode(node: data, di: di) })
        } else if (node is ViewTreeNode.Container.ContainerHorizontal) {
            let v = node as! ViewTreeNode.Container.ContainerHorizontal
            List(v.children, id: \.key, rowContent: { data in RenderNode(node: data, di: di) })
        } else if (node is ViewTreeNode.Leaf.LeafLabel) {
            let label = node as! ViewTreeNode.Leaf.LeafLabel
            Text(label.text)
        } else if (node is ViewTreeNode.Leaf.LeafRectangle) {
            let rect = node as! ViewTreeNode.Leaf.LeafRectangle
            Rectangle()
                    .fill(Color.red)
                    .frame(width: CGFloat(rect.width), height: CGFloat(rect.height))
        } else if (node is ViewTreeNode.Leaf.LeafButton) {
            let button = node as! ViewTreeNode.Leaf.LeafButton
            Button(action: {
                di.sendButtonPressedIntent(buttonId: button.id)
            }) {
                Text(button.text)
            }
        } else if (node is ViewTreeNode.Leaf.LeafImage) {
            let img = node as! ViewTreeNode.Leaf.LeafImage
            Rectangle()
                    .fill(Color.green)
                    .frame(width: CGFloat(img.width), height: CGFloat(img.height))
        } else if (node is ViewTreeNode.Leaf.LeafInput) {
            let input = node as! ViewTreeNode.Leaf.LeafInput
            let value = di.getStoreValue(key: input.storageKey)
            RenderInputTextView(label: input.hint, value: value) { inputValueStr in
                di.sendUpdateClientStorageIntent(key: input.storageKey, value: inputValueStr)
            }
        } else {
            Text("unknown node type, node: \(node)")
        }
    }
}
