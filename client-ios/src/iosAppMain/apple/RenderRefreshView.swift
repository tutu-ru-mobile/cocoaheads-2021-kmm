import SwiftUI
import ClientShared

struct RenderRefreshView: View {
    var state:RefreshViewState
    var di:IosStoreHelper

    var body: some View {
        if (state.serverData is RefreshViewState.ServerDataLoading) {
            Text("Loading...")
        } else if (state.serverData is RefreshViewState.ServerDataLoaded) {
            let serverDataLoaded = state.serverData as! RefreshViewState.ServerDataLoaded
            RenderNode(
                    node: serverDataLoaded.node,
                    di: di
            )
        }
    }
}
