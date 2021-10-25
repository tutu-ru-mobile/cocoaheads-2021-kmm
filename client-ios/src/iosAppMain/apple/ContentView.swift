import SwiftUI
import ClientShared

struct ContentView: View {

    let appDiIos = IosStoreHelper()
    @ObservedObject var myViewModel:GlobalViewModel

    init() {
        self.myViewModel = GlobalViewModel(di: appDiIos)
    }

    var body: some View {
        RenderRefreshView(state: appDiIos.getLastState(), di: appDiIos)
    }
}
