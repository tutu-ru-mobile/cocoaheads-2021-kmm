import Foundation
import shared

public class GlobalViewModel: ObservableObject {
    @Published public var myState: GlobalState

    public init(di:AppDi) {
        myState = di.getLastState()
        di.addListener(listener: {state in
            self.myState = state
        })
    }

}
