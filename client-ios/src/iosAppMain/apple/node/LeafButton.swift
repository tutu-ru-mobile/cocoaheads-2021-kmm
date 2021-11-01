import SwiftUI
import Shared

public struct LeafButton: View {
    let nodeButton: ViewTreeNode.Leaf.LeafButton
    let clientStorage: ClientStorage
    let sendIntent: (ClientIntent) -> Void

    public init(_ nodeButton: ViewTreeNode.LeafButton, _ clientStorage: ClientStorage, _ sendIntent: @escaping (ClientIntent) -> ()) {
        self.nodeButton = nodeButton
        self.clientStorage = clientStorage
        self.sendIntent = sendIntent
    }

    public var body: some View {
        Button(action: {
            sendIntent(SwiftHelperKt.buttonIntent(buttonId: nodeButton.id))
        }) {
            Text(nodeButton.text)
                    .font(.system(size: CGFloat(nodeButton.fontSize)))
        }.padding(3)
    }

}
