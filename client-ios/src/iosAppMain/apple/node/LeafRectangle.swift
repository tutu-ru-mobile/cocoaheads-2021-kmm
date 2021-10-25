import SwiftUI
import Shared

public struct LeafRectangle: View {
    let rectangle: ViewTreeNode.Leaf.LeafRectangle

    public init(_ rectangle: ViewTreeNode.Leaf.LeafRectangle) {
        self.rectangle = rectangle
    }

    public var body: some View {
        Rectangle()
                .fill(Color(rectangle.color.toUIColor()))
                .frame(width: CGFloat(rectangle.width), height: CGFloat(rectangle.height))
    }

}
