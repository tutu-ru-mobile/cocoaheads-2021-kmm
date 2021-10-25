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
        NavigationView {
            if (state.serverData is RefreshViewState.ServerDataLoading) {
                Text("Loading...")
            } else if (state.serverData is RefreshViewState.ServerDataLoaded) {
                let serverDataLoaded = state.serverData as! RefreshViewState.ServerDataLoaded
                RenderNode(serverDataLoaded.node, state.clientStorage) { (intent: ClientIntent) in
                    iosStoreHelper.sendAction(a: intent)
                }.navigationBarTitle("Важная информация", displayMode: .inline)
            }
        }
    }
}
