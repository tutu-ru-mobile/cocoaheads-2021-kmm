import SwiftUI
import Shared

public struct LeafText: View {
    let text: ViewTreeNode.Leaf.LeafText

    public init(_ text: ViewTreeNode.Leaf.LeafText) {
        self.text = text
    }

    public var body: some View {
        Text(text.text)
                .font(.system(size: CGFloat(text.fontSize)))
                .multilineTextAlignment(.center)
    }

}
