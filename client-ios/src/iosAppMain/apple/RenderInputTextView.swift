import SwiftUI

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
