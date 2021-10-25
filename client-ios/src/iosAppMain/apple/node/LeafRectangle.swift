import SwiftUI
import Shared

public struct LeafRectangle: View {
    let rectangle: ViewTreeNode.Leaf.LeafRectangle
    let iosStoreHelper:IosStoreHelper

    public init(_ rectangle: ViewTreeNode.Leaf.LeafRectangle, _ iosStoreHelper:IosStoreHelper) {
        self.rectangle = rectangle
        self.iosStoreHelper = iosStoreHelper
    }

    public var body: some View {
        let int32Color = iosStoreHelper.convertUIntToInt(value: rectangle.color)

        Rectangle()
                .fill(Color(uiColor: UIColor(rgb: Int(int32Color))))
                .frame(width: CGFloat(rectangle.width), height: CGFloat(rectangle.height))
    }

}

extension UIColor {
    convenience init(red: Int, green: Int, blue: Int) {
        assert(red >= 0 && red <= 255, "Invalid red component")
        assert(green >= 0 && green <= 255, "Invalid green component")
        assert(blue >= 0 && blue <= 255, "Invalid blue component")

        self.init(red: CGFloat(red) / 255.0, green: CGFloat(green) / 255.0, blue: CGFloat(blue) / 255.0, alpha: 1.0)
    }

    convenience init(rgb: Int) {
        self.init(
                red: (rgb >> 16) & 0xFF,
                green: (rgb >> 8) & 0xFF,
                blue: rgb & 0xFF
        )
    }
}
