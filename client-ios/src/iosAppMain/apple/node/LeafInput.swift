import SwiftUI
import Shared

public struct LeafInput: View {
    let input: ViewTreeNode.Leaf.LeafInput
    let clientStorage: ClientStorage
    let sendIntent: (ClientIntent) -> Void

    public init(_ input: ViewTreeNode.LeafInput, _ clientStorage: ClientStorage, _ sendIntent: @escaping (ClientIntent) -> ()) {
        self.input = input
        self.clientStorage = clientStorage
        self.sendIntent = sendIntent
    }

    public var body: some View {
        let value = clientStorage.getString(key: input.storageKey)
        RenderInputTextView(label: input.hint, value: value) { inputValueStr in
            sendIntent(SwiftHelperKt.updateClientStorageIntent(key: input.storageKey, value: inputValueStr))
        }
    }

}

public struct RenderInputTextView: View {
    var label: String
    var onEdit: (String) -> Void
    var value: String

    public init(label: String, value: String, onEdit: @escaping (String) -> ()) {
        self.label = label
        self.onEdit = onEdit
        self.value = value
    }

    func getBoundValue() -> Binding<String> {
        Binding<String>(get: { () -> String in
            self.value
        }, set: { s in
            self.onEdit(s)
        })
    }

    public var body: some View {
        HStack {
            Text(label)
            TextField("почта", text: getBoundValue())
                    .font(Font.system(size: 15, weight: .medium, design: .serif))
                    .textFieldStyle(RoundedBorderTextFieldStyle())
        }
                .padding()
    }
}

