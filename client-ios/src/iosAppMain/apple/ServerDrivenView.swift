import SwiftUI
import Shared

public struct ServerDrivenView: View {
    let mviStore: ServerDrivenViewStoreWrapper

    @ObservedObject var myViewModel: ServerDrivenViewModel

    public init(userId: String, networkReducerUrl: String, autoUpdate: Bool, sideEffectHandler: @escaping (ClientSideEffect) -> Void) {
        mviStore = ServerDrivenViewStoreWrapper(
                userId: userId,
                networkReducerUrl: networkReducerUrl,
                autoUpdate: autoUpdate,
                sideEffectHandler: sideEffectHandler
        )
        self.myViewModel = ServerDrivenViewModel(mviStore)
    }

    public var body: some View {
        switch myViewModel.myState.screen {
        case let loadingScreen as ServerDrivenViewState.ServerDrivenViewScreenLoading:
            Text("Loading...")
        case let normalScreen as ServerDrivenViewState.ServerDrivenViewScreenNormal:
            RenderNode(normalScreen.node, myViewModel.myState.clientStorage) { (intent: ClientIntent) in
                mviStore.sendIntent(intent: intent)
            }
        case let errorScreen as ServerDrivenViewState.ServerDrivenViewScreenNetworkError:
            VStack {
                Text("Сетевая ошибка")
                Text(errorScreen.exception)
            }
        default:
            Text("wrong ServerDrivenViewState myViewModel.myState.screen: \(myViewModel.myState.screen)")
        }
    }
}
