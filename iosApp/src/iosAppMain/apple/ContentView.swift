import SwiftUI
import shared
import SerializedData

func greet() -> String {
    return Greeting().greeting()
}

let root = SerializedDataKt.getDefaultNode()

struct ContentView: View {

    let appDiIos = AppDi()
    @ObservedObject var myViewModel:GlobalViewModel

    init() {
        self.myViewModel = GlobalViewModel(di: appDiIos)
    }


    var body: some View {
        VStack {
            RenderNode(node: root, state: appDiIos.getLastState(), di: appDiIos)
            Text(greet())
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

struct RenderNode: View {
    var node:Node
    var state:GlobalState
    var di:AppDi

    var body: some View {
        if (node is Node.Container.ContainerV) {
            let v = node as! Node.Container.ContainerV
            List(v.children, id: \.key, rowContent: { data in RenderNode(node: data, state: state, di: di) })
        } else if (node is Node.Container.ContainerH) {
            let v = node as! Node.Container.ContainerH
            List(v.children, id: \.key, rowContent: { data in RenderNode(node: data, state: state, di: di) })
        } else if (node is Node.Leaf.LeafLabel) {
            let label = node as! Node.Leaf.LeafLabel
            Text(label.text)
        } else if (node is Node.Leaf.LeafRectangle) {
            let rect = node as! Node.Leaf.LeafRectangle
            Rectangle()
                    .fill(Color.red)
                    .frame(width: CGFloat(rect.width), height: CGFloat(rect.height))
        } else if (node is Node.Leaf.LeafButton) {
            let button = node as! Node.Leaf.LeafButton
            Button(action: {
                di.sendAction(a: GlobalAction.ActionInput(str: "Button pressed"))
                print("Button pressed")
                //todo send intent
            }) {
                Text(button.text)
            }
        } else if (node is Node.Leaf.LeafImage) {
            let img = node as! Node.Leaf.LeafImage
            Rectangle()
                    .fill(Color.green)
                    .frame(width: CGFloat(img.width), height: CGFloat(img.height))
        } else if (node is Node.Leaf.LeafInput) {
            let input = node as! Node.Leaf.LeafInput
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
