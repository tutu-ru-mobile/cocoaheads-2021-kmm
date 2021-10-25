import SwiftUI
import Shared

public struct RenderRefreshView: View {
    let iosStoreHelper = IosStoreHelper()
    @ObservedObject var myViewModel:GlobalViewModel

    public init(/*todo side effect lambda*/) {
        self.myViewModel = GlobalViewModel(di: iosStoreHelper)
    }

    public var body: some View {
        let state = myViewModel.myState
        if (state.serverData is RefreshViewState.ServerDataLoading) {
            Text("Loading...")
        } else if (state.serverData is RefreshViewState.ServerDataLoaded) {
            let serverDataLoaded = state.serverData as! RefreshViewState.ServerDataLoaded
            RenderNode(
                    node: serverDataLoaded.node,
                    iosStoreHelper: iosStoreHelper
            )
        }
    }
}
