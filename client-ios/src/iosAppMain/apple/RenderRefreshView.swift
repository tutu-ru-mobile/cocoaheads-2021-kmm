import SwiftUI
import Shared

public struct RenderRefreshView: View {
    let swiftStoreHelper:SwiftStoreHelper

    @ObservedObject var myViewModel:GlobalViewModel

    public init(userId:String, sideEffectHandler: @escaping (ClientSideEffect) -> Void) {
        swiftStoreHelper = SwiftStoreHelper(userId: userId, sideEffectHandler: sideEffectHandler)
        self.myViewModel = GlobalViewModel(swiftStoreHelper)
    }

    public var body: some View {
        let state = myViewModel.myState
        if (state.serverData is RefreshViewState.ServerDataLoading) {
            Text("Loading...")
        } else if (state.serverData is RefreshViewState.ServerDataLoaded) {
            let serverDataLoaded = state.serverData as! RefreshViewState.ServerDataLoaded
            RenderNode(serverDataLoaded.node, state.clientStorage) { (intent: ClientIntent) in
                swiftStoreHelper.sendAction(a: intent)
            }
        }

    }
}
