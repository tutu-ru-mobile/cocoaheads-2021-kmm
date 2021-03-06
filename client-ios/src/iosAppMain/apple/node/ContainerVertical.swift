import SwiftUI
import Shared

public struct ContainerVertical:View {
    let containerVertical:ViewTreeNode.Container.ContainerVertical
    let clientStorage: ClientStorage
    let sendIntent: (ClientIntent) -> Void

    public init(_ containerVertical: ViewTreeNode.ContainerVertical, _ clientStorage: ClientStorage, _ sendIntent: @escaping (ClientIntent) -> ()) {
        self.containerVertical = containerVertical
        self.clientStorage = clientStorage
        self.sendIntent = sendIntent
    }

    public var body: some View {
        if (true) {
            VStack(alignment: containerVertical.horizontalAlignment.toSwiftAlignment()) {
                ForEach(containerVertical.children) { child in
                    RenderNode(child, clientStorage, sendIntent)
                }
            }.background(Color(containerVertical.backgroundColor.toUIColor()))
                    .padding(2)
        } else {
            // Вертикальный контейнер можно было сделать с помощью List
            List(containerVertical.children) { child in
                RenderNode(child, clientStorage, sendIntent)
            }.background(Color(containerVertical.backgroundColor.toUIColor()))
                    .padding(1)
        }
    }

}

extension HAlign {
    func toSwiftAlignment() -> HorizontalAlignment {
        switch self {
        case is HAlign.Left:
            return HorizontalAlignment.leading
        case is HAlign.Center:
            return HorizontalAlignment.center
        case is HAlign.Right:
            return HorizontalAlignment.trailing
        default:
            fatalError("HAlign.toSwiftAlignment() not exhaustive")
        }
    }
}
