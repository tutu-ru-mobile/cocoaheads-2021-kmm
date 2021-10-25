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
            VStack {
                ForEach(containerVertical.children) { child in
                    RenderNode(child, clientStorage, sendIntent)
                }
            }
        } else {
            // Вертикальный контейнер можно было сделать с помощью List
            List(containerVertical.children) { child in
                RenderNode(child, clientStorage, sendIntent)
            }
        }
    }

}
