import SwiftUI
import Shared

public struct RenderRefreshView: View {
    let mviStore: RefreshViewStoreWrapper

    @ObservedObject var myViewModel: RefreshViewModel

    public init(userId: String, networkReducerUrl: String, autoUpdate: Bool, sideEffectHandler: @escaping (ClientSideEffect) -> Void) {
        mviStore = RefreshViewStoreWrapper(
                userId: userId,
                networkReducerUrl: networkReducerUrl,
                autoUpdate: autoUpdate,
                sideEffectHandler: sideEffectHandler
        )
        self.myViewModel = RefreshViewModel(mviStore)
    }

    public var body: some View {
        switch myViewModel.myState.screen {
        case let loadingScreen as RefreshViewState.RefreshViewScreenLoading:
            Text("Loading...")
        case let normalScreen as RefreshViewState.RefreshViewScreenNormal:
            RenderNode(normalScreen.node, myViewModel.myState.clientStorage) { (intent: ClientIntent) in
                mviStore.sendIntent(intent: intent)
            }
        case let errorScreen as RefreshViewState.RefreshViewScreenNetworkError:
            VStack {
                Text("Сетевая ошибка")
                Text(errorScreen.exception)
            }
        default:
            Text("wrong RefreshViewState myViewModel.myState.screen: \(myViewModel.myState.screen)")
        }
    }
}
