import Foundation
import Shared

public class GlobalViewModel: ObservableObject {
    @Published public var myState: RefreshViewState

    public init(_ swiftStoreHelper:SwiftStoreHelper) {
        myState = swiftStoreHelper.getLastState()
        swiftStoreHelper.addListener(listener: {state in
            self.myState = state
        })
    }

}
