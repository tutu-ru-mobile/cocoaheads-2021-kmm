import SwiftUI
import ClientShared
import SerializedData

let root = SerializedDataKt.getDefaultNode()//todo delete

struct ContentView: View {

    let appDiIos = AppDi()
    @ObservedObject var myViewModel:GlobalViewModel

    init() {
        self.myViewModel = GlobalViewModel(di: appDiIos)
    }

    var body: some View {
        RenderNode(node: root, state: appDiIos.getLastState(), di: appDiIos)
    }
}

struct RenderNode: View {
    var node:ViewTreeNode
    var state:GlobalState
    var di:AppDi

    var body: some View {
        if (node is ViewTreeNode.Container.ContainerVertical) {
            let v = node as! ViewTreeNode.Container.ContainerVertical
            List(v.children, id: \.key, rowContent: { data in RenderNode(node: data, state: state, di: di) })
        } else if (node is ViewTreeNode.Container.ContainerHorizontal) {
            let v = node as! ViewTreeNode.Container.ContainerHorizontal
            List(v.children, id: \.key, rowContent: { data in RenderNode(node: data, state: state, di: di) })
        } else if (node is ViewTreeNode.Leaf.LeafLabel) {
            let label = node as! ViewTreeNode.Leaf.LeafLabel
            Text(label.text)
        } else if (node is ViewTreeNode.Leaf.LeafRectangle) {
            let rect = node as! ViewTreeNode.Leaf.LeafRectangle
            Rectangle()
                    .fill(Color.red)
                    .frame(width: CGFloat(rect.width), height: CGFloat(rect.height))
        } else if (node is ViewTreeNode.Leaf.LeafButton) {
            let button = node as! ViewTreeNode.Leaf.LeafButton
            Button(action: {
                di.sendAction(a: GlobalAction.ActionInput(str: "Button pressed"))
                print("Button pressed")
                //todo send intent
            }) {
                Text(button.text)
            }
        } else if (node is ViewTreeNode.Leaf.LeafImage) {
            let img = node as! ViewTreeNode.Leaf.LeafImage
            Rectangle()
                    .fill(Color.green)
                    .frame(width: CGFloat(img.width), height: CGFloat(img.height))
        } else if (node is ViewTreeNode.Leaf.LeafInput) {
            let input = node as! ViewTreeNode.Leaf.LeafInput
            InputTextView(label: input.hint, value: state.str) { inputValueStr in
                di.sendAction(a: GlobalAction.ActionInput(str: inputValueStr))
                //todo save storage by key
            }
        }
    }
}

struct InputTextView: View {
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
