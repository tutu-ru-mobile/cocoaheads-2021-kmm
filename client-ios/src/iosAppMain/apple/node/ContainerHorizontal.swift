import SwiftUI
import Shared

public struct ContainerHorizontal: View {
    let containerHorizontal: ViewTreeNode.Container.ContainerHorizontal
    let clientStorage: ClientStorage
    let sendIntent: (ClientIntent) -> Void

    public init(_ containerHorizontal: ViewTreeNode.ContainerHorizontal, _ clientStorage: ClientStorage, _ sendIntent: @escaping (ClientIntent) -> ()) {
        self.containerHorizontal = containerHorizontal
        self.clientStorage = clientStorage
        self.sendIntent = sendIntent
    }

    public var body: some View {
        HStack(alignment: containerHorizontal.verticalAlignment.toSwiftAlignment()) {
            ForEach(containerHorizontal.children) { child in
                RenderNode(child, clientStorage, sendIntent)
            }
        }.frame(maxWidth: .infinity)
        .background(Color(containerHorizontal.backgroundColor.toUIColor()))
        .padding(2)
    }

}

extension VAlign {
    func toSwiftAlignment() -> VerticalAlignment {
        switch self {
        case is VAlign.Bottom:
            return VerticalAlignment.bottom
        case is VAlign.Center:
            return VerticalAlignment.center
        case is VAlign.Top:
            return VerticalAlignment.top
        default:
            fatalError("VAlign.toSwiftAlignment() not exhaustive")
        }
    }
}
