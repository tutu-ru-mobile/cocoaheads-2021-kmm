import SwiftUI
import Shared

public struct LeafImage: View {
    let nodeImage: ViewTreeNode.Leaf.LeafImage

    public init(_ nodeImage: ViewTreeNode.Leaf.LeafImage) {
        self.nodeImage = nodeImage
    }

    public var body: some View {
        AsyncImage(url: URL(string: nodeImage.imgUrl), scale: CGFloat(nodeImage.scale))
                .frame(width: CGFloat(nodeImage.width), height: CGFloat(nodeImage.height))
    }

}
