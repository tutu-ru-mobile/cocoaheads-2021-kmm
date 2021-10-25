import SwiftUI
import Shared

struct RenderNode: View {
    var node: NSObject
    var iosStoreHelper: IosStoreHelper

    var body: some View {
        if (node is ViewTreeNode.Container.ContainerVertical) {
            let v = node as! ViewTreeNode.Container.ContainerVertical
            if (true) {
                VStack {
                    ForEach(v.children) { child in
                        RenderNode(node: child, iosStoreHelper: iosStoreHelper)
                    }
                }
            } else {
                // Вертикальный контейнер можно было сделать с помощью List
                List(v.children) { data in
                    RenderNode(node: data, iosStoreHelper: iosStoreHelper)
                }
            }
        } else if (node is ViewTreeNode.Container.ContainerHorizontal) {
            let v = node as! ViewTreeNode.Container.ContainerHorizontal
            HStack {
                ForEach(v.children) { child in
                    RenderNode(node: child, iosStoreHelper: iosStoreHelper)
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
                iosStoreHelper.sendButtonPressedIntent(buttonId: button.id)
            }) {
                Text(button.text)
            }
        } else if (node is ViewTreeNode.Leaf.LeafImage) {
            let img = node as! ViewTreeNode.Leaf.LeafImage
            AsyncImage(url: URL(string: img.imgUrl), scale: CGFloat(img.scale))
                    .frame(width: CGFloat(img.width), height: CGFloat(img.height))
        } else if (node is ViewTreeNode.Leaf.LeafInput) {
            let input = node as! ViewTreeNode.Leaf.LeafInput
            let value = iosStoreHelper.getClientStorageValue(key: input.storageKey)
            RenderInputTextView(label: input.hint, value: value) { inputValueStr in
                iosStoreHelper.sendUpdateClientStorageIntent(key: input.storageKey, value: inputValueStr)
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

