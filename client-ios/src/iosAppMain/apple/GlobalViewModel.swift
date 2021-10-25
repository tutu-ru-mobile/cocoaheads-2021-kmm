import Foundation
import ClientShared

public class GlobalViewModel: ObservableObject {
    @Published public var myState: RefreshViewState

    public init(di:IosStoreHelper) {
        myState = di.getLastState()
        di.addListener(listener: {state in
            self.myState = state
        })
    }

}
