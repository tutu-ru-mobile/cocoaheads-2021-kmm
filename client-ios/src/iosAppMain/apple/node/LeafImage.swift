import SwiftUI
import Shared

public struct LeafImage: View {
    let nodeImage: ViewTreeNode.Leaf.LeafImage

    public init(_ nodeImage: ViewTreeNode.Leaf.LeafImage) {
        self.nodeImage = nodeImage
    }

    public var body: some View {
        AsyncImage(url: URL(string: nodeImage.imgUrl), content: { image in
            image.resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(maxWidth: CGFloat(nodeImage.width), maxHeight: CGFloat(nodeImage.width))
        }, placeholder: {
            ProgressView()
        })
    }

}
