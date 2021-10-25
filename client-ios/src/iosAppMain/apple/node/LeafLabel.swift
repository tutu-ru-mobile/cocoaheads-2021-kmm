import SwiftUI
import Shared

public struct LeafLabel: View {
    let label: ViewTreeNode.Leaf.LeafLabel

    public init(_ label: ViewTreeNode.Leaf.LeafLabel) {
        self.label = label
    }

    public var body: some View {
        Text(label.text)
    }

}
