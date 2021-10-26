import Foundation
import Shared

public class RefreshViewModel: ObservableObject {
    @Published public var myState: RefreshViewState

    public init(_ mviStore:RefreshViewStoreWrapper) {
        myState = mviStore.getLastState()
        mviStore.addListener(listener: {state in
            self.myState = state
        })
    }

}
