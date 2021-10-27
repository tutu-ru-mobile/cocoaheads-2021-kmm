import Foundation
import Shared

public class ServerDrivenViewModel: ObservableObject {
    @Published public var myState: ServerDrivenViewState

    public init(_ mviStore:ServerDrivenViewStoreWrapper) {
        myState = mviStore.getLastState()
        mviStore.addListener(listener: {state in
            self.myState = state
        })
    }

}
